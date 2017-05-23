package ca.qc.inspq.securite.commun;

import java.io.Serializable;

public class Hello implements Serializable {
	private static final long serialVersionUID = -2487030245799063868L;

	private String hello;

	public Hello() {}

	public Hello(String hello) {
		this.hello = hello;
	}

	public void setHello(String nom) {
		this.hello = nom;
	}

	public String getHello() {
		return hello;
	}

	@Override
	public String toString() {
		return String.format("{\"hello\":%s}", hello);
	}
}
