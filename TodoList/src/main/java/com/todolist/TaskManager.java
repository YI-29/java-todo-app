package com.todolist;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

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
	
	// 更新処理（画面とDBの両方）
	public void updateTask(JPanel rowPanel, String oldTaskText, String oldDeadlineText, JLabel label, JButton updateButton) {
	    // ラベルを隠す
	    label.setVisible(false);
	    updateButton.setVisible(false);

	    // 編集用日付選択
	    rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
	    JDateChooser editDeadlineChooser = new JDateChooser();
	    editDeadlineChooser.setDateFormatString("yyyy-MM-dd");
	    editDeadlineChooser.setMaximumSize(new Dimension(150, 25));
	    
	    // 編集用テキストフィールド
	    JTextField editTaskField = new JTextField(oldTaskText, 15);
	    editTaskField.setMaximumSize(new Dimension(150, 25));

	    //変更前の日付が入力欄に表示される設定
	    try {
	        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDeadlineText);
	        editDeadlineChooser.setDate(date);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // 保存＆キャンセルボタン
	    JButton saveButton = new JButton("保存");
	    JButton cancelButton = new JButton("キャンセル");

	    // 一時表示
	    rowPanel.add(editDeadlineChooser);
	    rowPanel.add(Box.createHorizontalStrut(10));
	    rowPanel.add(editTaskField);
	    rowPanel.add(Box.createHorizontalStrut(10));
	    rowPanel.add(saveButton);
	    rowPanel.add(Box.createHorizontalStrut(10));
	    rowPanel.add(cancelButton);
	    rowPanel.revalidate();
	    rowPanel.repaint();

	    // 保存ボタン処理
	    saveButton.addActionListener(e -> {
	        String newTask = editTaskField.getText();
	        Date newDeadline = editDeadlineChooser.getDate();
	        if (newTask.isEmpty() || newDeadline == null) {
	            JOptionPane.showMessageDialog(view, "タスクまたは期限日が未入力です");
	            return;
	        }
	        String newDeadlineStr = new SimpleDateFormat("yyyy-MM-dd").format(newDeadline);

	        try {
	            // DB更新
	            dao.updateTask(oldTaskText, oldDeadlineText, newTask, newDeadlineStr);
	            // 表示更新
	            label.setText("期限：" + newDeadlineStr + "　タスク：" + newTask);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(view, "DB更新エラー: " + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // 編集UIを削除し元に戻す
	        rowPanel.remove(editTaskField);
	        rowPanel.remove(editDeadlineChooser);
	        rowPanel.remove(saveButton);
	        rowPanel.remove(cancelButton);
	        label.setVisible(true);
	        updateButton.setVisible(true);

	        rowPanel.revalidate();
	        rowPanel.repaint();
	    });

	    // キャンセルボタン処理
	    cancelButton.addActionListener(e -> {
	        rowPanel.remove(editTaskField);
	        rowPanel.remove(editDeadlineChooser);
	        rowPanel.remove(saveButton);
	        rowPanel.remove(cancelButton);
	        label.setVisible(true);
	        updateButton.setVisible(true);
	        rowPanel.revalidate();
	        rowPanel.repaint();
	    });
	}

}
