package sii.letscode.contentProviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Hubert on 20.11.2015.
 */
public class DatabaseContentProvider extends ContentProvider {
    private static final String DB_NAME = "sii.letscode.db";
    private static final int DB_VERSION = 1;

    private static final String DB_PROPERTIES = "properties";
    private static final String DB_PROPERTIES_ID = "id";
    private static final String DB_PROPERTIES_VALUE = "value";

    private static final String DB_PROPERTIES_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DB_PROPERTIES +
                    " (" + DB_PROPERTIES_ID + " VARCHAR(128) NOT NULL, " +
                            DB_PROPERTIES_VALUE + " VARCHAR(1000) NOT NULL, " +
                            "CONSTRAINT id_unique UNIQUE (" + DB_PROPERTIES_ID + ")" +
                    ");";

    private static final String DB_PROPERTIES_DROP =
            "DROP TABLE IF EXISTS " + DB_PROPERTIES;

    private static final String CONTENT = "content://";
    private static final String BASE_PATH_PROPERTIES = "properties";

    private static final int URI_MATCHER_PROPERTIES = 10;
    private static final int URI_MATCHER_PROPERTIES_ID = 11;

    private static final String PROVIDER_NAME = "sii.letscode.contentProviders";

    private static final Uri CONTENT_URI_PROPERTIES = Uri.parse(CONTENT + PROVIDER_NAME + "/" + DB_PROPERTIES);

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PROVIDER_NAME, BASE_PATH_PROPERTIES, URI_MATCHER_PROPERTIES);
        uriMatcher.addURI(PROVIDER_NAME, BASE_PATH_PROPERTIES + "/#", URI_MATCHER_PROPERTIES_ID);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        this.dbHelper = new DBHelper(getContext());
        this.database = this.dbHelper.getWritableDatabase();
        return (database == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_PROPERTIES:
                qb.setTables(DB_PROPERTIES);
                break;
            case URI_MATCHER_PROPERTIES_ID:
                qb.setTables(DB_PROPERTIES);
                qb.appendWhere(DB_PROPERTIES_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        this.database = dbHelper.getReadableDatabase();
        Cursor c = qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        this.database = dbHelper.getWritableDatabase();
        Uri result;

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_PROPERTIES:
                result = ContentUris.withAppendedId(CONTENT_URI_PROPERTIES, this.database.insertOrThrow(DB_PROPERTIES, null, values));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_PROPERTIES:
                count = this.database.delete(DB_PROPERTIES, selection, selectionArgs);
                break;
            case URI_MATCHER_PROPERTIES_ID:
                count = this.database.delete(DB_PROPERTIES, DB_PROPERTIES_ID + " = " + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_PROPERTIES:
                count = this.database.update(DB_PROPERTIES, values, selection, selectionArgs);
                break;
            case URI_MATCHER_PROPERTIES_ID:
                count = this.database.update(DB_PROPERTIES, values, DB_PROPERTIES_ID + " = " + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return count;
    }

    protected static final class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DB_PROPERTIES_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DB_PROPERTIES_DROP);
        }
    }
}
