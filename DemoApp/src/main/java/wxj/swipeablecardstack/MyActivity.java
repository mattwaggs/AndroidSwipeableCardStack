package wxj.swipeablecardstack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wenchao.cardstack.CardStack;


public class MyActivity extends Activity {

    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mCardStack = (CardStack)findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.card_content);

        mCardAdapter = new CardsDataAdapter(getApplicationContext());

        for (int i =0; i< 25; i ++) {
            mCardAdapter.add("Card: " + (i+1));
        }

        mCardStack.setAdapter(mCardAdapter);

        if(mCardStack.getAdapter() != null) {
            Log.i("MyActivity", "Card Stack size: " + mCardStack.getAdapter().getCount());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
