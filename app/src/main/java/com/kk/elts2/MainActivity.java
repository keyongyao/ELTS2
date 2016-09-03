package com.kk.elts2;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import com.kk.elts2.activities.BaseActivity;
import com.kk.elts2.biz.IExamBiz;

public class MainActivity extends BaseActivity {
    RadioButton mSaveAll,mSaveID,mSaveNull ;
    EditText mEtID,mEtPWD;
    IExamBiz mBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        initView();
    }

    private void initView() {
        mSaveAll= findViewById_(R.id.saveAll);
        mSaveID= findViewById_(R.id.saveID);
        mSaveNull= findViewById_(R.id.saveNull);
        mEtID= findViewById_(R.id.etID);
        mEtPWD= findViewById_(R.id.etPwd);

    }

    /**
     * 右上角菜单
     * @param menu  菜单XML
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
