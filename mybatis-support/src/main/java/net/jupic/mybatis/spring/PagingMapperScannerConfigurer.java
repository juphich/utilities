package net.jupic.mybatis.spring;

import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

/**
 * 
 * @author chang jung pil
 *
 */
public class PagingMapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor,
													  InitializingBean, 
													  ApplicationContextAware, 
													  BeanNameAware {

	//field group for scanning mappers
	private String basePackage;
	private boolean addToConfig = true;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;
	
	//field group for mapper factory
	private String sqlSessionTemplateBeanName;
	private String sqlSessionFactoryBeanName;	
	
	//field group for bean definition
	private ApplicationContext applicationContext;
	private String beanName;
	private boolean processPropertyPlaceHolders;
	  

	/**
	 * @param basePackage the basePackage to set
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * @param addToConfig the addToConfig to set
	 */
	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
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
	 * @param processPropertyPlaceHolders the processPropertyPlaceHolders to set
	 */
	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
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
	 * @param arg0
	 */
	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	/**
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		notNull(this.basePackage, "Property 'basePackage' is required");
	}

	/**
	 * @param arg0
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}
	
	/**
	 * @param arg0
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		if (this.processPropertyPlaceHolders) {
			processPropertyPlaceHolders();
		}
		MapperScanner scanner = new MapperScanner(beanDefinitionRegistry);
		
		scanner.setResourceLoader(this.applicationContext);
	    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, 
	    		ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
	
	/*
	 * BeanDefinitionRegistries are called early in application startup, before
	 * BeanFactoryPostProcessors. This means that PropertyResourceConfigurers will not have been
	 * loaded and any property substitution of this class' properties will fail. To avoid this, find
	 * any PropertyResourceConfigurers defined in the context and run them on this class' bean
	 * definition. Then update the values.
	 */
	private void processPropertyPlaceHolders() {
		Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		if (!prcs.isEmpty() && applicationContext instanceof GenericApplicationContext) {
			BeanDefinition mapperScannerBean = ((GenericApplicationContext) applicationContext)
					.getBeanFactory().getBeanDefinition(beanName);

			// PropertyResourceConfigurer does not expose any methods to explicitly perform
			// property placeholder substitution. Instead, create a BeanFactory that just
			// contains this mapper scanner and post process the factory.
			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			factory.registerBeanDefinition(beanName, mapperScannerBean);

			for (PropertyResourceConfigurer prc : prcs.values()) {
				prc.postProcessBeanFactory(factory);
			}

			PropertyValues values = mapperScannerBean.getPropertyValues();

			this.basePackage = updatePropertyValue("basePackage", values);
			this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", values);
			this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", values);
		}
	}

	private String updatePropertyValue(String propertyName, PropertyValues values) {
		PropertyValue property = values.getPropertyValue(propertyName);

		if (property == null) {
			return null;
		}

		Object value = property.getValue();

		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return value.toString();
		} else if (value instanceof TypedStringValue) {
			return ((TypedStringValue) value).getValue();
		} else {
			return null;
		}
	}
	
	/**
	 * This class scans classes for declared mapper interfaces and register them.
	 * This class extends ClassPathBeanDefinition Scanner class
	 * 
	 * @author chang jung pil
	 *
	 */
	private final class MapperScanner extends ClassPathBeanDefinitionScanner {

		/**
		 * @param registry
		 */
		public MapperScanner(BeanDefinitionRegistry registry) {
			super(registry);
		}

		/**
	     * Configures parent scanner to search for the right interfaces. It can search for all
	     * interfaces or just for those that extends a markerInterface or/and those annotated with
	     * the annotationClass
	     */
		@Override
		protected void registerDefaultFilters() {
			boolean acceptAllInterfaces = true;

			// if specified, use the given annotation and / or marker interface
			if (PagingMapperScannerConfigurer.this.annotationClass != null) {
				addIncludeFilter(new AnnotationTypeFilter(PagingMapperScannerConfigurer.this.annotationClass));
				acceptAllInterfaces = false;
			}

			// override AssignableTypeFilter to ignore matches on the actual marker interface
			if (PagingMapperScannerConfigurer.this.markerInterface != null) {
				addIncludeFilter(new AssignableTypeFilter(PagingMapperScannerConfigurer.this.markerInterface) {
					@Override
					protected boolean matchClassName(String className) {
						return false;
					}
				});
				acceptAllInterfaces = false;
			}

			if (acceptAllInterfaces) {
				// default include filter that accepts all classes
				addIncludeFilter(new TypeFilter() {
					@Override
					public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
							throws IOException {
						return true;
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
	     * Calls the parent search that will search and register all the candidates. Then the
	     * registered objects are post processed to set them as MapperFactoryBeans
	     */
		@Override
		protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
			Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
			
			if (beanDefinitions.isEmpty()) {
				logger.warn("No MyBatis mapper was found in '" + PagingMapperScannerConfigurer.this.basePackage
						+ "' package. Please check your configuration.");
			} else {
				for (BeanDefinitionHolder holder : beanDefinitions) {
					
					GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

					if (logger.isDebugEnabled()) {
						logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName()
								+ "' and '" + definition.getBeanClassName() + "' mapperInterface");
					}
					
					// the mapper interface is the original class of the bean
					// but, the actual class of the bean is MapperFactoryBean
					definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
					definition.setBeanClass(PagingMapperFactoryBean.class);
					definition.getPropertyValues().add("addToConfig", PagingMapperScannerConfigurer.this.addToConfig);
					
					// checking sql session factory of mybatis object and inject it 
					boolean explicitFactoryUsed = false;
					if (StringUtils.hasLength(PagingMapperScannerConfigurer.this.sqlSessionFactoryBeanName)) {
						definition.getPropertyValues().add("sqlSessionFactory", 
								new RuntimeBeanReference(PagingMapperScannerConfigurer.this.sqlSessionFactoryBeanName));
						explicitFactoryUsed = true;
					}
					
					if (StringUtils.hasLength(PagingMapperScannerConfigurer.this.sqlSessionTemplateBeanName)) {
						if (explicitFactoryUsed) {
							logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
						}
						definition.getPropertyValues().add("sqlSessionTemplate",
								new RuntimeBeanReference(PagingMapperScannerConfigurer.this.sqlSessionTemplateBeanName));
						definition.getPropertyValues().add("sqlSessionFactory", null);
					}
				}
			}
			
			return beanDefinitions;
		}
		
		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
		}

		@Override
		protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
			if (super.checkCandidate(beanName, beanDefinition)) {
				return true;
			} else {
				logger.warn("Skipping MapperFactoryBean with name '" + beanName
						+ "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
						+ ". Bean already defined with the same name!");
				return false;
			}
		}
	}
}
