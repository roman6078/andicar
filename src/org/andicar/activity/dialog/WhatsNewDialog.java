 /*
 * AndiCar - car management software for Android powered devices
 * Copyright (C) 2010 - 2011 Miklos Keresztes (miklos.keresztes@gmail.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT AY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.andicar.activity.dialog;

import org.andicar2.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Miklos Keresztes
 *
 */
public class WhatsNewDialog extends Activity{
	/* (non-Javadoc)
	 * @see org.andicar.activity.EditActivityBase#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.whatsnew_dialog);
        TextView tv = (TextView)findViewById(R.id.tvText1);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("UpdateMsg")){
        	CharSequence updMsg = Html.fromHtml(extras.getString("UpdateMsg"));
        	tv.setText(updMsg);
        }
        

        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);        
        ImageButton btnOk = (ImageButton)findViewById(android.R.id.button1);
        btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
