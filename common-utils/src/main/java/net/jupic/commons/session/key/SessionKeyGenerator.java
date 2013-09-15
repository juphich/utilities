package net.jupic.commons.session.key;

import java.io.Serializable;

public interface SessionKeyGenerator<T extends Serializable> {
	T generateKey();
}