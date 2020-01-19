package sll.plugin.helper.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import org.jetbrains.annotations.NotNull;
import sll.plugin.helper.model.MainUIData;
import sll.plugin.helper.ui.InfoUI;
import sll.plugin.helper.ui.MainUI;

import java.util.Arrays;

/**
 * Created by LSL on 2020/1/7 11:30
 */
public class MainAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//        anActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);
        new InfoUI(anActionEvent);
//        MainUIData mainUIData = new MainUIData();
//        mainUIData.setConnectionSettings(null);
//        mainUIData.setSelectedTables(Arrays.asList("222", "333"));
//        // 打开主窗口
//        new MainUI(anActionEvent, mainUIData);
    }
}
