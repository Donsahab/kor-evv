package dextrous.kor.evv.korevv.util;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.MenuRes;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MyPopupMenu extends PopupMenu
        implements MenuBuilder.Callback,
        MenuPresenter.Callback {

    private Context mContext;
    private MenuBuilder mMenu;
    private View mAnchor;
    private MenuPopupHelper mPopup;
    private OnMenuItemClickListener mMenuItemClickListener;
    private OnDismissListener mDismissListener;
    private View.OnTouchListener mDragListener;

    /**
     * Callback interface used to notify the application that the menu has closed.
     */
    public interface OnDismissListener {
        /**
         * Called when the associated menu has been dismissed.
         *
         * @param menu The PopupMenu that was dismissed.
         */
        public void onDismiss(PopupMenu menu);
    }

    /**
     * Construct a new PopupMenu.
     *
     * @param context Context for the PopupMenu.
     * @param anchor  Anchor view for this popup. The popup will appear below the anchor if there
     *                is room, or above it if there is not.
     */
    public MyPopupMenu(Context context, View anchor) {
        this(context, anchor, Gravity.NO_GRAVITY);
    }

    /**
     * Constructor to create a new popup menu with an anchor view and alignment
     * gravity.
     *
     * @param context Context the popup menu is running in, through which it
     *                can access the current theme, resources, etc.
     * @param anchor  Anchor view for this popup. The popup will appear below
     *                the anchor if there is room, or above it if there is not.
     * @param gravity The {@link Gravity} value for aligning the popup with its
     *                anchor.
     */
    public MyPopupMenu(Context context, View anchor, int gravity) {
        this(context, anchor, gravity, androidx.appcompat.R.attr.popupMenuStyle, 0);
    }

    /**
     * Constructor a create a new popup menu with a specific style.
     *
     * @param context        Context the popup menu is running in, through which it
     *                       can access the current theme, resources, etc.
     * @param anchor         Anchor view for this popup. The popup will appear below
     *                       the anchor if there is room, or above it if there is not.
     * @param gravity        The {@link Gravity} value for aligning the popup with its
     *                       anchor.
     * @param popupStyleAttr An attribute in the current theme that contains a
     *                       reference to a style resource that supplies default values for
     *                       the popup window. Can be 0 to not look for defaults.
     * @param popupStyleRes  A resource identifier of a style resource that
     *                       supplies default values for the popup window, used only if
     *                       popupStyleAttr is 0 or can not be found in the theme. Can be 0
     *                       to not look for defaults.
     */
    @SuppressLint("RestrictedApi")
    public MyPopupMenu(Context context, View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr,
                popupStyleRes);
        mContext = context;
        mMenu = new MenuBuilder(context);
        mMenu.setCallback(this);
        mAnchor = anchor;
        mPopup = new MenuPopupHelper(context, mMenu, anchor, false, popupStyleAttr, popupStyleRes);
        mPopup.setGravity(gravity);
//        mPopup.setCallback(this);
        mPopup.setForceShowIcon(true);

    }

    /**
     * Returns an {@link android.view.View.OnTouchListener} that can be added to the anchor view
     * to implement drag-to-open behavior.
     * <p>
     * When the listener is set on a view, touching that view and dragging
     * outside of its bounds will open the popup window. Lifting will select the
     * currently touched list item.
     * <p>
     * Example usage:
     * <pre>
     * PopupMenu myPopup = new PopupMenu(context, myAnchor);
     * myAnchor.setOnTouchListener(myPopup.getDragToOpenListener());
     * </pre>
     *
     * @return a touch listener that controls drag-to-open behavior
     */
//    public View.OnTouchListener getDragToOpenListener() {
//        if (mDragListener == null) {
//            mDragListener = new ListPopupWindow.ForwardingListener(mAnchor) {
//                @Override
//                protected boolean onForwardingStarted() {
//                    show();
//                    return true;
//                }
//
//                @Override
//                protected boolean onForwardingStopped() {
//                    dismiss();
//                    return true;
//                }
//
//                @Override
//                public ListPopupWindow getPopup() {
//                    // This will be null until show() is called.
//                    return mPopup.getPopup();
//                }
//            };
//        }
//
//        return mDragListener;
//    }

    /**
     * @return the {@link Menu} associated with this popup. Populate the returned Menu with
     * items before calling {@link #show()}.
     * @see #show()
     * @see #getMenuInflater()
     */
    public Menu getMenu() {
        return mMenu;
    }

    /**
     * @return a {@link MenuInflater} that can be used to inflate menu items from XML into the
     * menu returned by {@link #getMenu()}.
     * @see #getMenu()
     */
    @SuppressLint("RestrictedApi")
    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(mContext);
    }

    /**
     * Inflate a menu resource into this PopupMenu. This is equivalent to calling
     * popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu()).
     *
     * @param menuRes Menu resource to inflate
     */
    public void inflate(@MenuRes int menuRes) {
        getMenuInflater().inflate(menuRes, mMenu);
    }

    /**
     * Show the menu popup anchored to the view specified during construction.
     *
     * @see #dismiss()
     */
    @SuppressLint("RestrictedApi")
    public void show() {
        mPopup.show();
    }

    /**
     * Dismiss the menu popup.
     *
     * @see #show()
     */
    @SuppressLint("RestrictedApi")
    public void dismiss() {
        mPopup.dismiss();
    }

    /**
     * Set a listener that will be notified when the user selects an item from the menu.
     *
     * @param listener Listener to notify
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mMenuItemClickListener = listener;
    }

    /**
     * Set a listener that will be notified when this menu is dismissed.
     *
     * @param listener Listener to notify
     */
    public void setOnDismissListener(OnDismissListener listener) {
        mDismissListener = listener;
    }

    /**
     * @hide
     */
    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
        if (mMenuItemClickListener != null) {
            return mMenuItemClickListener.onMenuItemClick(item);
        }
        return false;
    }

    /**
     * @hide
     */
    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        if (mDismissListener != null) {
            mDismissListener.onDismiss(this);
        }
    }

    /**
     * @hide
     */
    @SuppressLint("RestrictedApi")
    public boolean onOpenSubMenu(MenuBuilder subMenu) {
        if (subMenu == null) return false;

        if (!subMenu.hasVisibleItems()) {
            return true;
        }

        // Current menu will be dismissed by the normal helper, submenu will be shown in its place.
        new MenuPopupHelper(mContext, subMenu, mAnchor).show();
        return true;
    }

    /**
     * @hide
     */
    public void onCloseSubMenu(SubMenuBuilder menu) {
    }

    /**
     * @hide
     */
    public void onMenuModeChange(MenuBuilder menu) {
    }

    /**
     * Interface responsible for receiving menu item click events if the items themselves
     * do not have individual item click listeners.
     */
    public interface OnMenuItemClickListener {
        /**
         * This method will be invoked when a menu item is clicked if the item itself did
         * not already handle the event.
         *
         * @param item {@link MenuItem} that was clicked
         * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
         */
        public boolean onMenuItemClick(MenuItem item);
    }
}
