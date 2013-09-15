package net.jupic.commons.parser;

import java.io.IOException;
import java.io.InputStream;

import net.jupic.commons.parser.exception.MessageParsingException;


public interface MessageParser<D> {

	D parse(InputStream source) throws IOException, MessageParsingException;
}
