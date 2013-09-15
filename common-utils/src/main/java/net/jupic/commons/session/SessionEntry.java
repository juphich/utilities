package net.jupic.commons.session;

import java.io.Serializable;
import java.util.Set;

/**
 * @author chang jung pil
 *
 */
public interface SessionEntry extends Serializable {
	
	Serializable getAttribute(String key);

	Set<String> keySet();
}
