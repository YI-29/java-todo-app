package com.todolist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TaskManager extends JFrame implements ActionListener  {
	
	private View view;
	private TaskDAO dao;

	public TaskManager(View view) {
		this.view = view;
		this.dao = new TaskDAO();
	}
	
	//登録ボタンが押下されたときの処理
	public void actionPerformed(ActionEvent e) {
		String deadlineText = view.getSelectedDeadline();
		String taskText = view.task.getText();
		
		//フォーマットチェック
        if (taskText.isEmpty() || deadlineText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "タスクまたは期限日が未入力です");
            return;
        }else if(taskText.equals("タスクを入力")) {
        	JOptionPane.showMessageDialog(view, "タスクを入力してください");
            return;
        }
		
        //リストにタスクを表示
		String input = "期限：" + deadlineText + "　タスク：" + taskText;
	    view.displayArea.append(input + "\n"); 
	    
	    //INSERT文を実行
	    try {
            dao.insertTask(taskText, deadlineText);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "DB登録エラー: " + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
        }
	}
}
