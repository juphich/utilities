package net.jupic.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.jupic.commons.exception.UtilityExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author chang jung pil
 *
 */
public class ShellExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(ShellExecutor.class);
	
	private String os;
	private String[] commands;
	private StringBuilder output;
	
	private String shellEncoding = "UTF-8";

	public ShellExecutor(String command) {
		this.os = System.getProperty("os.name");
		if (os.toLowerCase().indexOf("windows") > -1) {
			this.commands = new String[2];
			commands[0] = "cmd";
			commands[1] = "/c";
			
			shellEncoding = "MS949";
		} else {
			this.commands = new String[0];
		}
		setCommand(command);
	}
	
	public void setCommand(String command) {
		if (command != null) {
			String[] inputs = command.split("[\\s]");
			
			String[] temp = new String[commands.length + inputs.length];
			System.arraycopy(commands, 0, temp, 0, commands.length);
			System.arraycopy(inputs, 0, temp, commands.length, inputs.length);
			
			this.commands = temp;
		}
	}
	
	public void setShellEncoding(String shellEncoding) {
		this.shellEncoding = shellEncoding;
	}

	public static String execute(String command) {
		return new ShellExecutor(command).execute();
	}
	
	public String execute() {
		Process process = null;
		BufferedReader stdOut = null;
		BufferedReader stdErr = null;
		
		this.output = new StringBuilder();
		
		try {
			process = new ProcessBuilder(commands).start();
			stdOut = new BufferedReader(new InputStreamReader(process.getInputStream(), shellEncoding));
			stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream(), shellEncoding));
			
			String message = null;
			while ((message = stdOut.readLine()) != null) {
				output.append(message).append(System.getProperty("line.separator"));
			}
			
			while ((message = stdErr.readLine()) != null) {
				output.append(message).append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			logger.error("command execution failed!! ", e);
			throw new UtilityExecutionException("command execution failed!!", e);
		} finally {
			process.destroy();
			if (stdOut != null) {
				try {
					stdOut.close();
				} catch (IOException e) {
					if (logger.isDebugEnabled()) {
						logger.error("buffer input stream exception : {}", e.getMessage());
					}
				}
			}
			
			if (stdErr != null) {
				try {
					stdErr.close();
				} catch (IOException e) {
					if (logger.isDebugEnabled()) {
						logger.error("buffer input stream exception : {}", e.getMessage());
					}
				}
			}
		}
		
		return output.toString();
	}
}
