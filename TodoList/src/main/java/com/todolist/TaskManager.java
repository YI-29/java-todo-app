package com.todolist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	    view.addTaskPanel(taskText, deadlineText, this);
	    
	    //DBに登録
	    try {
            dao.insertTask(taskText, deadlineText);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "DB登録エラー: " + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
        }
	    // 入力欄リセット
	    view.task.setText("タスクを入力");
	}
	
	// 削除処理（画面とDBの両方）
	public void deleteTask(JPanel rowPanel, String taskText, String deadlineText) {
	    int confirm = JOptionPane.showConfirmDialog(view, "このタスクを削除しますか？", "確認", JOptionPane.YES_NO_OPTION);
	    if (confirm == JOptionPane.YES_OPTION) {
	        // 表示から削除
	        view.taskListPanel.remove(rowPanel);
	        view.taskListPanel.revalidate();
	        view.taskListPanel.repaint();

	        // DBから削除
	        try {
	            dao.deleteTask(taskText, deadlineText); 
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(view, "DB削除エラー: " + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
}
