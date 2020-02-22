package work.xujiyou.view.model;

import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.SortableColumnModel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * KubernetesTableModel class
 *
 * @author jiyouxu
 * @date 2020/2/19
 */
public class KubernetesTableModel implements TableModel, SortableColumnModel {

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "haha";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return "haha";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    @Override
    public ColumnInfo[] getColumnInfos() {
        return new ColumnInfo[0];
    }

    @Override
    public void setSortable(boolean aBoolean) {

    }

    @Override
    public boolean isSortable() {
        return false;
    }

    @Override
    public Object getRowValue(int row) {
        return null;
    }

    @Nullable
    @Override
    public RowSorter.SortKey getDefaultSortKey() {
        return null;
    }
}
