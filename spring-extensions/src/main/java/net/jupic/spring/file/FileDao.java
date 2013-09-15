package net.jupic.spring.file;

/**
 * @author chang jung pil
 *
 * @param <D>
 */
public interface FileDao<D extends FileMetadata> {

	void add(D fileInfo);

	void delete(Object parameters);

	D get(Object parameters);
}
