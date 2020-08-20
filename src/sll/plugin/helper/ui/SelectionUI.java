package sll.plugin.helper.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import sll.plugin.helper.model.ConnectionSettings;
import sll.plugin.helper.model.MainUIData;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.utils.DatabaseUtil;
import sll.plugin.helper.utils.MessageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * 选择需要操作的表
 * <p>
 * Created by LSL on 2020/1/7 18:13
 */
public class SelectionUI extends JFrame {

    private ConnectionSettings connectionSettings;

    private JPanel contentPanel = new JPanel();
    private JPanel bntPanel = new JBPanel<>();

    private JPanel northPanel = new JBPanel<>();
    private JPanel centerPanel = new JBPanel<>();
    private JPanel southPanel = new JBPanel<>();

    // 按钮
    private JButton buttonCharge = new JButton("换 库");
    private JButton buttonNext = new JButton("下一步");
    private JButton buttonCancel = new JButton("取 消");
    private JLabel selectedNum = new JLabel("（已选：0）");

    public SelectionUI(AnActionEvent actionEvent, ConnectionSettings connectionSettings) throws Exception {
        this.connectionSettings = connectionSettings;

        northPanel.add(new JLabel("请选择需要操作的表[按Ctrl或Shift进行多选]"));
        northPanel.add(selectedNum);

        // 封装列表数据model
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(initData());
        JList<String> jList = new JBList<>(model);
        // 设置可多选
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane jScrollPane = new JBScrollPane(jList);
        jScrollPane.setPreferredSize(new Dimension(300, 580));
        centerPanel.add(jScrollPane);

        // 添加按钮控件
        bntPanel.setLayout(new BoxLayout(bntPanel, BoxLayout.X_AXIS));
        bntPanel.setBorder(JBUI.Borders.empty());
        bntPanel.add(buttonCharge);
        bntPanel.add(buttonNext);
        bntPanel.add(buttonCancel);
        southPanel.add(bntPanel);

        // 设置主窗体
        setTitle("选择表");
        getRootPane().setDefaultButton(buttonNext);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(contentPanel);
        contentPanel.setLayout(new BorderLayout(10, 2));
        contentPanel.add(northPanel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(340, 720));
        // 设置窗体屏幕居中
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - getPreferredSize().getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - getPreferredSize().getHeight()) / 2;
        setLocation(x, y);
        pack();
        setVisible(true);

        // 切换数据库
        buttonCharge.addActionListener(e -> {
            new InfoUI(actionEvent);
            // 关闭当前窗口
            onCancel();
        });

        // 添加点击事件
        buttonNext.addActionListener(e -> {
            List<String> selectedValuesList = jList.getSelectedValuesList();
            boolean isNext = selectedValuesList.size() > 0;
            if (isNext) {
                MainUIData mainUIData = new MainUIData();
                mainUIData.setConnectionSettings(connectionSettings);
                mainUIData.setSelectedTables(selectedValuesList);
                // 打开主窗口
                new MainUI(actionEvent, mainUIData);
                // 关闭当前窗口
                onCancel();
            } else {
                MessageUtil.warn("请选择需要操作的表");
            }
        });

        jList.addListSelectionListener(e -> {
            List<String> selectedValuesList = jList.getSelectedValuesList();
            selectedNum.setText("（已选：" + selectedValuesList.size() + "）");
        });

        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    /**
     * 初始化数据
     *
     * @return 数据库表
     * @throws Exception 空
     */
    private String[] initData() throws Exception {
        DatabaseUtil databaseUtil = new DatabaseUtil(connectionSettings);
        String[] tables = databaseUtil.getTables();
        if (tables.length == 0) {
            throw new BaseException("当前数据库无可操作表，请检查数据库");
        }
        return tables;
    }

    private void onCancel() {
        dispose();
    }
}
