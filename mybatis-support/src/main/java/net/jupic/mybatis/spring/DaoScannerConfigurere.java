package net.jupic.mybatis.spring;

import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

/**
 * @author Chang jung pil
 *
 */
public class DaoScannerConfigurere implements BeanDefinitionRegistryPostProcessor,
											  InitializingBean, 
											  ApplicationContextAware, 
											  BeanNameAware {

	//field group for scanning mappers
	private String basePackage;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;
	
	//field group for mapper factory
	private String sqlSessionTemplateBeanName;
	private String sqlSessionFactoryBeanName;	
	
	//field group for bean definition
	private ApplicationContext applicationContext;
	
	/**
	 * @param basePackage the basePackage to set
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * @param annotationClass the annotationClass to set
	 */
	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	/**
	 * @param markerInterface the markerInterface to set
	 */
	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}

	/**
	 * @param sqlSessionTemplateBeanName the sqlSessionTemplateBeanName to set
	 */
	public void setSqlSessionTemplateBeanName(String sqlSessionTemplateBeanName) {
		this.sqlSessionTemplateBeanName = sqlSessionTemplateBeanName;
	}

	/**
	 * @param sqlSessionFactoryBeanName the sqlSessionFactoryBeanName to set
	 */
	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
	}

	/**
	 * @param arg0
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	/**
	 * @param beanName
	 */
	@Override
	public void setBeanName(String beanName) {
	}

	/**
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		notNull(this.basePackage, "Property 'basePackage' is required");
		if ((annotationClass == null)&(markerInterface == null)) {
			notNull(annotationClass, "Property 'annotationClass' or 'markerInterface' is required");
			notNull(markerInterface, "Property 'annotationClass' or 'markerInterface' is required");
		}
	}

	/**
	 * @param beanDefinitionRegistry
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry)
			throws BeansException {
		DaoScanner scanner = new DaoScanner(beanDefinitionRegistry);
		
		scanner.setResourceLoader(this.applicationContext);
	    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, 
	    		ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
	
	private final class DaoScanner extends ClassPathBeanDefinitionScanner {

		/**
		 * @param registry
		 */
		public DaoScanner(BeanDefinitionRegistry registry) {
			super(registry);
		}
		
		/**
		 * 
		 */
		@Override
		protected void registerDefaultFilters() {
			// if specified, use the given annotation and / or marker interface
			if (DaoScannerConfigurere.this.annotationClass != null) {
				addIncludeFilter(new AnnotationTypeFilter(DaoScannerConfigurere.this.annotationClass));
			}
			
			if (DaoScannerConfigurere.this.markerInterface != null) {
				addIncludeFilter(new AssignableTypeFilter(DaoScannerConfigurere.this.markerInterface) {
					@Override
					protected boolean matchClassName(String className) {
						return false;
					}
				});
			}
						
			// exclude package-info.java
			addExcludeFilter(new TypeFilter() {
				@Override
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
						throws IOException {
					String className = metadataReader.getClassMetadata().getClassName();
					return className.endsWith("package-info");
				}
			});
		}

		/**
		 * @param basePackages
		 * @return
		 */
		@Override
		protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
			Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
			
			if (beanDefinitions.isEmpty()) {
				logger.warn("No Dao class was found in '" + DaoScannerConfigurere.this.basePackage
						+ "' package. Please check your configuration.");
			} else {
				for (BeanDefinitionHolder holder : beanDefinitions) {
					GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
					
					if (logger.isDebugEnabled()) {
						logger.debug("Registry Dao bean with name '" + holder.getBeanName()
								+ "' and '" + definition.getBeanClassName() + "' class");
					}
										
					// checking sql session factory of mybatis object and inject it 
					boolean explicitFactoryUsed = false;
					if (StringUtils.hasLength(DaoScannerConfigurere.this.sqlSessionFactoryBeanName)) {
						definition.getPropertyValues().add("sqlSessionFactory", 
								new RuntimeBeanReference(DaoScannerConfigurere.this.sqlSessionFactoryBeanName));
						explicitFactoryUsed = true;
					}
					
					if (StringUtils.hasLength(DaoScannerConfigurere.this.sqlSessionTemplateBeanName)) {
						if (explicitFactoryUsed) {
							logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
						}
						definition.getPropertyValues().add("sqlSessionTemplate",
								new RuntimeBeanReference(DaoScannerConfigurere.this.sqlSessionTemplateBeanName));
						definition.getPropertyValues().add("sqlSessionFactory", null);
					}
					
					registerBeanDefinition(holder, this.getRegistry());
				}
			}
			
			return beanDefinitions;
		}
	}
}
