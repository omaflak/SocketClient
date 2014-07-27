package com.omaflak.socketclient;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Serveur serveur = new Serveur(8632);
		try {
			serveur.start();
		} catch (IOException e) {
			System.out.println("Err start server : "+e.getMessage());
		}
	}
}
