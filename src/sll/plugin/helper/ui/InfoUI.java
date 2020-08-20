package sll.plugin.helper.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.SystemIndependent;
import sll.plugin.helper.enums.LocalCacheEnum;
import sll.plugin.helper.model.ConnectionSettings;
import sll.plugin.helper.utils.DatabaseUtil;
import sll.plugin.helper.utils.MessageUtil;
import sll.plugin.helper.utils.StateLocalCacheUtil;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 输入数据库连接信息UI
 * <p>
 * setting connection information
 * <p>
 * Created by LSL on 2020/1/7 11:23
 */
public class InfoUI extends JFrame {

    private AnActionEvent actionEvent;

    private JPanel contentPanel = new JBPanel<>();
    private JPanel filedPanel = new JBPanel<>();
    private JPanel bntPanel = new JBPanel<>();

    // 按钮
    private JButton buttonTest = new JButton("测试连接");
    private JButton buttonOK = new JButton("确 定");
    private JButton buttonCancel = new JButton("取 消");
//    private TextFieldWithBrowseButton tbuttonCancel = new TextFieldWithBrowseButton();

    /**
     * 文本框
     */
    // 主机
    private JTextField hostField = new JBTextField(20);
    // 端口
    private JTextField portField = new JBTextField();
    // 数据库名称
    private JTextField databaseField = new JBTextField();
    // 用户名
    private JTextField usernameField = new JBTextField();
    // 密码
    private JTextField passwordField = new JBPasswordField();

    public InfoUI(AnActionEvent actionEvent) {
        this.actionEvent = actionEvent;
        setTitle("配置连接信息");
        setPreferredSize(new Dimension(600, 300));
        // 设置窗体屏幕居中
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - getPreferredSize().getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - getPreferredSize().getHeight()) / 2;
        setLocation(x, y);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(contentPanel);

        filedPanel.setLayout(new GridLayout(5, 2));
        filedPanel.setBorder(JBUI.Borders.empty(8, 8, 2, 8));

        // 获取本地缓存信息
        ConnectionSettings connectionSettings = StateLocalCacheUtil.get(LocalCacheEnum.CONNECTION_SETTINGS_STATE);
        if (connectionSettings == null) {
            // 默认值
            connectionSettings = new ConnectionSettings();
            connectionSettings.setHost("127.0.0.1");
            connectionSettings.setPort("3306");
            connectionSettings.setUsername("root");
        }

        // 添加输入框控件
        filedPanel.add(getFiledPanel("主机地址:", connectionSettings.getHost(), hostField));
        filedPanel.add(getFiledPanel("端口:", connectionSettings.getPort(), portField));
        filedPanel.add(getFiledPanel("数据库:", connectionSettings.getDatabase(), databaseField));
        filedPanel.add(getFiledPanel("用户名:", connectionSettings.getUsername(), usernameField));
        filedPanel.add(getFiledPanel("密码:", connectionSettings.getPassword(), passwordField));
        contentPanel.add(filedPanel);

        // 添加按钮控件
        bntPanel.setLayout(new BoxLayout(bntPanel, BoxLayout.X_AXIS));
        bntPanel.setBorder(JBUI.Borders.empty(1, 5));
        bntPanel.add(buttonTest);
        bntPanel.add(buttonOK);
        bntPanel.add(buttonCancel);
//        bntPanel.add(tbuttonCancel);
        contentPanel.add(bntPanel);

        // 添加按钮监听
        buttonTest.addActionListener(e -> {
            if (testConnection()) {
                MessageUtil.success("连接成功");
            }
        });

        buttonOK.addActionListener(e -> {
            if (testConnection()) {
                try {
                    // 将连接信息缓存到本地
                    StateLocalCacheUtil.set(LocalCacheEnum.CONNECTION_SETTINGS_STATE, getConnectionSettings());
                    new SelectionUI(actionEvent, getConnectionSettings());
                } catch (Exception e1) {
                    MessageUtil.error(e1.getMessage());
                    return;
                }
                onCancel();
            }
        });

        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

//        tbuttonCancel.setTextFieldPreferredWidth(10);
//        Project project = actionEvent.getProject();
//        assert project != null;
//        String basePath = project.getBasePath();
//        System.out.println(basePath);
//        tbuttonCancel.setText(basePath);
//        tbuttonCancel.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleLocalFileDescriptor()) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                super.actionPerformed(e);
//                System.out.println(tbuttonCancel.getText());
//            }
//        });
    }

    /**
     * 测试连接
     */
    private boolean testConnection() {
        DatabaseUtil databaseUtil = new DatabaseUtil(getConnectionSettings());
        try {
            databaseUtil.connection();
            return true;
        } catch (Exception ex) {
            MessageUtil.error(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 获取连接设置
     *
     * @return ConnectionSettings
     */
    private ConnectionSettings getConnectionSettings() {
        ConnectionSettings connectionSettings = new ConnectionSettings();
        connectionSettings.setHost(hostField.getText().trim());
        connectionSettings.setPort(portField.getText().trim());
        connectionSettings.setDatabase(databaseField.getText().trim());
        connectionSettings.setUsername(usernameField.getText().trim());
        connectionSettings.setPassword(passwordField.getText().trim());
        return connectionSettings;
    }

    /**
     * 获取控件
     *
     * @param title
     * @param defaultValue
     * @param component
     * @return
     */
    private JPanel getFiledPanel(String title, String defaultValue, JTextComponent component) {
        component.setText(defaultValue);
        JPanel panel = new JBPanel<>();
        panel.setLayout(new GridLayout(1, 2));
        panel.setBorder(JBUI.Borders.empty(0));
        JLabel comp = new JLabel(title);
        comp.setPreferredSize(new Dimension(50, 30));
        panel.add(comp);
        panel.add(component);
        return panel;
    }

    private void onCancel() {
        dispose();
    }
}
