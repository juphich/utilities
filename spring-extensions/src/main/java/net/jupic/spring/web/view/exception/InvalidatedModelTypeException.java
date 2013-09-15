package net.jupic.spring.web.view.exception;

public class InvalidatedModelTypeException extends RuntimeException {

	private static final long serialVersionUID = -7438657452128332616L;

	private Object model;

	public InvalidatedModelTypeException(String message, Object model) {
		super(message + " model : " + model + ", " + model.getClass().getName());
		this.model = model;
	}
	
	public Object getModel() {
		return model;
	}
}
