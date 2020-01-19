package sll.plugin.helper.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.Gray;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import sll.plugin.helper.dto.*;
import sll.plugin.helper.generator.mybatis.GenerateMainTask;
import sll.plugin.helper.generator.unit.enums.LombokAnnotationEnum;
import sll.plugin.helper.generator.unit.enums.MapperMethodEnum;
import sll.plugin.helper.generator.unit.enums.NormalMethodEnum;
import sll.plugin.helper.model.MainUIData;
import sll.plugin.helper.utils.DatabaseUtil;
import sll.plugin.helper.utils.MessageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

/**
 * Created by LSL on 2020/1/8 15:54
 */
public class MainUI extends JFrame {

    private MainUIData mainUIData;
    private AnActionEvent actionEvent;

    private JPanel contentPanel = new JPanel();
    private JPanel btnPanel = new JBPanel<>();

    private JPanel westPanel = new JBPanel<>();
    private JPanel centerPanel = new JBPanel<>();
    private JPanel southPanel = new JBPanel<>();

    // 按钮
    private JButton buttonPrev = new JButton("重新选择");
    private JButton buttonDo = new JButton("生 成");
    private JButton buttonCancel = new JButton("取 消");
    private JButton buttonRemove = new JButton("移 除");

    private JLabel selectedNum = new JLabel("（共：0 项）");

    private JList<String> jList = new JBList<>();

    // 选择的表信息
    private String[] selectedTables;

    // center窗体元素
    private TextFieldWithBrowseButton buttonParentDir = new TextFieldWithBrowseButton();

    MainUI(AnActionEvent actionEvent, MainUIData mainUIData) {
        this.mainUIData = mainUIData;
        this.actionEvent = actionEvent;

        // 设置左边栏目
        westPanel.add(getLeftPanel());

        // 设置中间栏目
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        centerPanel.add(getParentPanel());
        centerPanel.add(getEntitySettingPanel());
        centerPanel.add(getDaoSettingPanel());
        centerPanel.add(getMapperSettingPanel());

        // 添加按钮控件
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(JBUI.Borders.empty());
        btnPanel.add(buttonPrev);
        btnPanel.add(buttonDo);
        btnPanel.add(buttonCancel);
        southPanel.add(btnPanel);

        // 设置主窗体
        setTitle("配置生成");
        getRootPane().setDefaultButton(buttonDo);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(contentPanel);
        contentPanel.setLayout(new BorderLayout(10, 2));
//        contentPanel.add(northPanel, BorderLayout.NORTH);
        contentPanel.add(westPanel, BorderLayout.WEST);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(880, 630));
        // 设置窗体屏幕居中
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - getPreferredSize().getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - getPreferredSize().getHeight()) / 2;
        setLocation(x, y);
        pack();
        setVisible(true);

        // 添加点击事件
        buttonDo.addActionListener(e -> {

            buttonDo.setEnabled(false);
            GenerateMainTaskDTO generateMainTaskDTO = new GenerateMainTaskDTO();
            // 获取生成表类信息
            try {
                generateMainTaskDTO.setColumnsMap(getTableColumnMap());
            } catch (Exception e1) {
                e1.printStackTrace();
                MessageUtil.error(e1.getMessage());
                return;
            }

            // 获取entity生成配置
            generateMainTaskDTO.setEntitySettingsDTO(getEntitySettingsDTO());

            // 获取dao生成配置
            generateMainTaskDTO.setDaoSettingsDTO(getDaoSettingsDTO());

            // 获取mapper生成配置
            generateMainTaskDTO.setMapperSettingsDTO(getMapperSettingsDTO());

            try {
                String elapsedTime = new GenerateMainTask(generateMainTaskDTO).execute();
                if (StringUtils.isNotBlank(elapsedTime)) {
                    MessageUtil.success("生成成功！总耗时：" + elapsedTime);
                }
                buttonDo.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                MessageUtil.error(ex.getMessage());
            }
        });

        // 选择父工程目录按钮
        buttonParentDir.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleLocalFileDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                System.out.println(buttonParentDir.getText());
            }
        });

        // 移除项
        buttonRemove.addActionListener(e -> {
            // 获取选择移除的项
            List<String> selectedValuesList = jList.getSelectedValuesList();
            if (selectedValuesList.isEmpty()) {
                MessageUtil.warn("请选择要移除项");
                return;
            }
            String[] selectedValues = new String[selectedTables.length - selectedValuesList.size()];
            int j = 0;
            for (String selectedTable : selectedTables) {
                if (!selectedValuesList.contains(selectedTable)) {
                    selectedValues[j++] = selectedTable;
                }
            }
            // 刷新列表
            DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>(selectedValues);
            jList.setModel(model2);
            selectedNum.setText("（共：" + selectedValues.length + " 项）");
            selectedTables = selectedValues;
        });

        buttonPrev.addActionListener(e -> {
            try {
                new SelectionUI(actionEvent, mainUIData.getConnectionSettings());
            } catch (Exception e1) {
                MessageUtil.error(e1.getMessage());
                return;
            }
            onCancel();
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
     * 获取表跟列信息关系
     *
     * @return map
     * @throws Exception 数据库操作异常
     */
    private Map<String, List<TableColumnDTO>> getTableColumnMap() throws Exception {
        List<String> tableList = Arrays.asList(selectedTables);
        DatabaseUtil databaseUtil = new DatabaseUtil(mainUIData.getConnectionSettings());
        return databaseUtil.getTableColumns(tableList);
    }

    /**
     * 获取mapper设置
     *
     * @return mapper settings
     */
    private MapperSettingsDTO getMapperSettingsDTO() {
        MapperSettingsDTO mapperSettingsDTO = new MapperSettingsDTO();
        mapperSettingsDTO.setParentPath(buttonParentDir.getText().trim());
        mapperSettingsDTO.setRelativePath(mapperPathField.getText().trim());
        mapperSettingsDTO.setPackageName(mapperPackageField.getText().trim());
        mapperSettingsDTO.setMethods(getMethodsList(methodCheckBoxList));
        return mapperSettingsDTO;
    }

    /**
     * 获取dao设置
     *
     * @return dao settings
     */
    private DaoSettingsDTO getDaoSettingsDTO() {
        DaoSettingsDTO daoSettingsDTO = new DaoSettingsDTO();
        daoSettingsDTO.setParentPath(buttonParentDir.getText().trim());
        daoSettingsDTO.setRelativePath(daoPathField.getText().trim());
        daoSettingsDTO.setPackageName(daoPackageField.getText().trim());
        daoSettingsDTO.setMapper(mapperCBox.isSelected());
        daoSettingsDTO.setRepository(repositoryCBox.isSelected());
        daoSettingsDTO.setMethods(getMethodsList(methodCheckBoxList));
        return daoSettingsDTO;
    }

    @NotNull
    private List<String> getMethodsList(List<JCheckBox> methodCheckBoxList) {
        List<String> list = new ArrayList<>();
        for (JCheckBox jCheckBox : methodCheckBoxList) {
            if (jCheckBox.isSelected()) {
                list.add(jCheckBox.getText());
            }
        }
        return list;
    }

    /**
     * 获取实体entity设置
     *
     * @return entity settings
     */
    private EntitySettingsDTO getEntitySettingsDTO() {
        EntitySettingsDTO entitySettingsDTO = new EntitySettingsDTO();
        entitySettingsDTO.setParentPath(buttonParentDir.getText().trim());
        entitySettingsDTO.setRelativePath(entityPathField.getText().trim());
        entitySettingsDTO.setPackageName(entityPackageField.getText().trim());
        entitySettingsDTO.setSerialize(serializeCBox.isSelected());
        entitySettingsDTO.setComment(commentCBox.isSelected());
        if (normalRButton.isSelected()) {
            entitySettingsDTO.setNormalMethods(getMethodsList(normalCheckBoxList));
        }
        if (lombokRButton.isSelected()) {
            entitySettingsDTO.setLombokAnnotations(getMethodsList(lombokCheckBoxList));
        }
        return entitySettingsDTO;
    }

    private static final int T1 = 1;
    private static final int T2 = 2;
    private static final int T3 = 3;

    private static final int COMMON_WIDTH = 610;
    private static final int COMMON_HEIGHT = 150;

    private JTextField mapperPathField = new JTextField("src/main/resources");
    private JTextField mapperPackageField = new JTextField("mapper");
    private Map<String, Boolean> methodTextMap = new LinkedHashMap<>();
    private List<JCheckBox> methodCheckBoxList = new ArrayList<>();

    @NotNull
    private JPanel getMapperSettingPanel() {
        JPanel panel = new JBPanel<>();
        panel.add(getTitlePanel("Mapper配置"));
        panel.add(getRow2Panel(mapperPathField, mapperPackageField));

        for (MapperMethodEnum value : MapperMethodEnum.values()) {
            methodTextMap.put(value.txt, value.def);
        }
//        methodTextMap.put("insert", true);
//        methodTextMap.put("delete", true);
//        methodTextMap.put("update", true);
//        methodTextMap.put("queryOne", true);
//        methodTextMap.put("queryAll", true);
//        methodTextMap.put("insertSelective", true);
//        methodTextMap.put("updateSelective", true);

        JLabel jLabel = new JLabel("可生成方法：");
        jLabel.setPreferredSize(new Dimension(600, 30));
        panel.add(jLabel);
        panel.add(getCheckBoxPanel(methodTextMap, 50, T3));

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(COMMON_WIDTH, (int) (COMMON_HEIGHT * 1.3)));
        panel.setBorder(JBUI.Borders.customLine(Gray._50));
        return panel;
    }

    private JTextField daoPathField = new JTextField("src/main/java");
    private JTextField daoPackageField = new JTextField("dao");
    private JCheckBox repositoryCBox = new JCheckBox("@Repository", true);
    private JCheckBox mapperCBox = new JCheckBox("@Mapper", false);

    @NotNull
    private JPanel getDaoSettingPanel() {
        JPanel panel = new JBPanel<>();
        panel.add(getTitlePanel("Dao配置"));

        panel.add(getRow2Panel(daoPathField, daoPackageField, repositoryCBox, mapperCBox));

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(COMMON_WIDTH, (int) (COMMON_HEIGHT * 0.7)));
        panel.setBorder(JBUI.Borders.customLine(Gray._50));
        return panel;
    }

    @NotNull
    private JPanel getRow2Panel(JTextField pathField, JTextField packageFiled, JCheckBox... checkBox) {
        JPanel rowPanel2 = new JBPanel<>();
//        rowPanel2.setPreferredSize(new Dimension(500, 30));
//        rowPanel2.setBorder(JBUI.Borders.customLine(new Color(92, 40, 28)));
        rowPanel2.setLayout(new BoxLayout(rowPanel2, BoxLayout.X_AXIS));
        pathField.setColumns(15);
        packageFiled.setColumns(13);
        packageFiled.setToolTipText("包名用.隔开");
        rowPanel2.add(new JLabel("生成路径: "));
        rowPanel2.add(pathField);
        rowPanel2.add(new JLabel("+ 包名: "));
        rowPanel2.add(packageFiled);
        for (JCheckBox box : checkBox) {
            rowPanel2.add(box);
        }
        return rowPanel2;
    }

    private JTextField entityPathField = new JTextField("src/main/java");
    private JTextField entityPackageField = new JTextField("entity");
    private JCheckBox commentCBox = new JCheckBox("数据库注解  ", true);
    private JCheckBox serializeCBox = new JCheckBox("序列化", true);

    private Map<String, Boolean> normalCbTextMap = new LinkedHashMap<>();
    private Map<String, Boolean> lombokCbTextMap = new LinkedHashMap<>();
    private List<JCheckBox> normalCheckBoxList = new ArrayList<>();
    private List<JCheckBox> lombokCheckBoxList = new ArrayList<>();

    private JRadioButton normalRButton = new JRadioButton("普通", true);
    private JRadioButton lombokRButton = new JRadioButton("lombok");

    private JPanel lombokCheckBoxPanel;
    private JPanel normalCheckBoxPanel;

    private void init() {
        // 普通文本
        for (NormalMethodEnum value : NormalMethodEnum.values()) {
            normalCbTextMap.put(value.txt, value.def);
        }
//        normalCbTextMap.put("Getter", true);
//        normalCbTextMap.put("Setter", true);
//        normalCbTextMap.put("toString", true);
//        normalCbTextMap.put("全参构造", false);
//        normalCbTextMap.put("无参构造", false);
//        normalCbTextMap.put("equals&hashCode", false);

        // lombok文本
        for (LombokAnnotationEnum value : LombokAnnotationEnum.values()) {
            lombokCbTextMap.put(value.txt, value.def);
        }
//        lombokCbTextMap.put("@Data", true);
//        lombokCbTextMap.put("@AllArgsConstructor", false);
//        lombokCbTextMap.put("@NoArgsConstructor", false);
//        lombokCbTextMap.put("@ToString", false);
//        lombokCbTextMap.put("@Accessors(chain=true)", false);
//        lombokCbTextMap.put("@Getter", false);
//        lombokCbTextMap.put("@Setter", false);

        ButtonGroup group = new ButtonGroup();
        group.add(normalRButton);
        group.add(lombokRButton);
    }

    @NotNull
    private JPanel getEntitySettingPanel() {
        JPanel panel = new JBPanel<>();
        panel.add(getTitlePanel("实体配置"));

        panel.add(getRow2Panel(entityPathField, entityPackageField, commentCBox, serializeCBox));

        init();
        panel.add(normalRButton);
        panel.add(lombokRButton);
        normalCheckBoxPanel = getCheckBoxPanel(normalCbTextMap, 25, T1);
        panel.add(normalCheckBoxPanel);
        lombokCheckBoxPanel = getCheckBoxPanel(lombokCbTextMap, 50, T2);
        lombokCheckBoxPanel.setVisible(false);
        panel.add(lombokCheckBoxPanel);
        // 添加点击事件
        normalRButton.addItemListener(this::radioButtonChange);
        lombokRButton.addItemListener(this::radioButtonChange);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(COMMON_WIDTH, COMMON_HEIGHT));
        panel.setBorder(JBUI.Borders.customLine(Gray._50));
        return panel;
    }

    private void radioButtonChange(ItemEvent e) {
        // 普通
        if (e.getSource().equals(normalRButton)) {
            normalCheckBoxPanel.setVisible(true);
            lombokCheckBoxPanel.setVisible(false);
        } else {
            normalCheckBoxPanel.setVisible(false);
            lombokCheckBoxPanel.setVisible(true);
        }
    }

    @NotNull
    private JPanel getCheckBoxPanel(Map<String, Boolean> map, Integer boxHeight, Integer type) {
        JPanel panel = new JBPanel<>();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(560, boxHeight));
//        panel.setBorder(JBUI.Borders.customLine(new Color(22, 99, 8)));
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            JCheckBox checkBox = new JCheckBox(entry.getKey(), entry.getValue());
            panel.add(checkBox);
            if (type.equals(T1)) {
                normalCheckBoxList.add(checkBox);
            } else if (type.equals(T2)) {
                lombokCheckBoxList.add(checkBox);
            } else if (type.equals(T3)) {
                methodCheckBoxList.add(checkBox);
            }
        }
        return panel;
    }

    @NotNull
    private JPanel getTitlePanel(String title) {
        JPanel panel = new JBPanel<>(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 30));
        JLabel tLabel = new JLabel(title);
        tLabel.setFont(new Font("黑体", Font.BOLD, 13));
        panel.add(tLabel);
        return panel;
    }

    @NotNull
    private JPanel getParentPanel() {
        // 设置中间栏目,title
        JPanel parentProjectPanel = new JBPanel<>();
        parentProjectPanel.setLayout(new BoxLayout(parentProjectPanel, BoxLayout.X_AXIS));
        buttonParentDir.setTextFieldPreferredWidth(45);
        Project project = actionEvent.getProject();
        if (project != null) {
            String basePath = project.getBasePath();
            buttonParentDir.setText(basePath);
        }
        parentProjectPanel.add(new JLabel("选择父工程目录："));
        parentProjectPanel.add(buttonParentDir);
        return parentProjectPanel;
    }

    @NotNull
    private JPanel getLeftPanel() {
        JPanel leftPanel = new JBPanel<>();
        leftPanel.setLayout(new BorderLayout(10, 3));
        leftPanel.add(selectedNum, BorderLayout.NORTH);
        // 封装列表数据model
        selectedTables = mainUIData.getSelectedTables().toArray(new String[0]);
        selectedNum.setText("（共：" + selectedTables.length + " 项）");
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(selectedTables);
        jList.setModel(model);
        // 设置可多选
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane jScrollPane = new JBScrollPane(jList);
        jScrollPane.setPreferredSize(new Dimension(180, 480));
        leftPanel.add(jScrollPane, BorderLayout.CENTER);
        leftPanel.add(buttonRemove, BorderLayout.SOUTH);
        return leftPanel;
    }

    private void onCancel() {
        dispose();
    }
}
