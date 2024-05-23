package view;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.List;

/**
 * TransferHandler that handles drag and drop of items in a JList.
 *
 * @param <E> the type of the items in the data model
 */
class ListTransferHandler<E> extends TransferHandler {
    private final JList<E> list;
    private final DefaultListModel<E> listModel;
    private final List<E> dataModel;
    private int[] indices = null;
    private int addIndex = -1;
    private int addCount = 0;

    public ListTransferHandler(JList<E> list, List<E> dataModel) {
        this.list = list;
        this.listModel = (DefaultListModel<E>) list.getModel();
        this.dataModel = dataModel;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        indices = list.getSelectedIndices();
        List<E> values = list.getSelectedValuesList();
        return new DataHandler<>(values);
    }

    @Override
    public boolean canImport(TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }
        return info.isDataFlavorSupported(DataHandler.FLAVOR);
    }

    @Override
    public boolean importData(TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }

        JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
        int index = dl.getIndex();
        boolean insert = dl.isInsert();

        Transferable t = info.getTransferable();
        List<E> data;
        try {
            data = (List<E>) t.getTransferData(DataHandler.FLAVOR);
        } catch (UnsupportedFlavorException | IOException e) {
            return false;
        }

        if (insert) {
            for (E value : data) {
                listModel.add(index, value);
                dataModel.add(index++, value);
            }
            addIndex = dl.getIndex();
            addCount = data.size();
        } else {
            for (E value : data) {
                listModel.set(index, value);
                dataModel.set(index++, value);
            }
            addIndex = -1;
            addCount = 0;
        }
        return true;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if ((action == MOVE) && (indices != null)) {
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }

            for (int i = indices.length - 1; i >= 0; i--) {
                listModel.remove(indices[i]);
                dataModel.remove(indices[i]);
            }
        }
        indices = null;
        addIndex = -1;
        addCount = 0;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    static class DataHandler<E> implements Transferable {
        static final DataFlavor FLAVOR = new DataFlavor(List.class, "List of Items");
        private final List<E> data;

        DataHandler(List<E> data) {
            this.data = data;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }
    }
}
