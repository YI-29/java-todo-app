package com.todolist;

import java.awt.EventQueue;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoListApplication {
	
	public static void main(String[] args) {
		View frame = new View();
		
		//Viewクラスで設定した画面を表示させる
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
