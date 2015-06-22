package com.wojtek.wstatus;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class WTimelineFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	private final static String TAG = WTimelineFragment.class.getSimpleName();
	private SimpleCursorAdapter adapter;
	private static final int LOADER_ID = 1;

	private static final String[] FROM = { WStatusContract.Column.USER,
			WStatusContract.Column.MESSAGE, WStatusContract.Column.CREATED_AT };
	private static final int[] TO = { R.id.list_item_text_user,
			R.id.list_item_text_message, R.id.list_item_text_created_at };

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// connect directly data with view
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item,
				null, FROM, TO, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		adapter.setViewBinder(new MyViewBinder());
		setListAdapter(adapter);

		getLoaderManager().initLoader(LOADER_ID, null, this);

	}

	// Executed on non-UI thread - here the data is actually loaded
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		if (id != LOADER_ID)
			return null;

		return new CursorLoader(getActivity(), WStatusContract.ContentURI,
				null, null, null, WStatusContract.DEFAULT_SORT);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		
		adapter.swapCursor(cursor); // we update the data that adapter using to
									// update view

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
		adapter.swapCursor(null);

	}

	private class MyViewBinder implements ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.list_item_text_created_at)
				return false;

			long time = cursor.getLong(cursor
					.getColumnIndex(WStatusContract.Column.CREATED_AT));
			CharSequence properTime = DateUtils.getRelativeTimeSpanString(time);
			((TextView) view).setText(properTime);
			return true;
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, final long id) {
		super.onListItemClick(l, v, position, id);

		AlertDialog.Builder alert = new AlertDialog.Builder(
				WTimelineFragment.this.getActivity());

		alert.setMessage(R.string.dialog_del1item);
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				getActivity().getContentResolver().delete(WStatusContract.ContentURI,
						WStatusContract.Column.ID + " = " + id, null);
			}
		});
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.cancel();
				
			}
		});
		alert.show();

		
	}

}
