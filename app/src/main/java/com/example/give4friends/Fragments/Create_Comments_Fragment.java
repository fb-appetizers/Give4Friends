package com.example.give4friends.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.Comments;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

public class Create_Comments_Fragment extends Fragment {

    String charityName;
    ParseUser user;
    ParseObject charity;
    EditText etcommentMessage;
    TextView tvCharityName;
    Button commentSubmitBtn;
    ImageView ivProfileCreateComments;

    TextView tvhandlerComments;

    public Create_Comments_Fragment(String charityName, ParseUser user, Charity charity) {
        this.charityName = charityName;
        this.user = user;
        this.charity = charity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etcommentMessage = view.findViewById(R.id.etcommentMessage);
        tvCharityName = view.findViewById(R.id.tvCommentsCharityName);
        commentSubmitBtn = view.findViewById(R.id.commentSubmitBtn);
        ivProfileCreateComments = view.findViewById(R.id.ivProfileCreateComments);
        tvhandlerComments = view.findViewById(R.id.tvhandlerComments);

        tvCharityName.setText(charityName);
        tvhandlerComments.setText("@"+user.getUsername());
        String imageURL = user.getString("profileImageURL");






        if(imageURL != null){
            Date imageDate = user.getDate("profileImageCreatedAt");
            Glide.with(getContext())
                    .load(imageURL)
                    .apply(new RequestOptions()
                                    .transforms(new CircleCrop(), new RoundedCorners(20))
                                    .circleCrop()
                                    .signature(new ObjectKey(imageDate))
//                                    .placeholder(R.drawable.instagram_user_outline_24)

                    )

                    .into(ivProfileCreateComments);
        }
        else{
            Glide.with(getContext())
                    .load(R.drawable.instagram_user_outline_24)
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCrop())
                    .into(ivProfileCreateComments);
        }


        etcommentMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    commentSubmitBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    commentSubmitBtn.setTextColor(getResources().getColor(R.color.colorPrimary));

                    commentSubmitBtn.setOnClickListener(null);
                }else{
                    commentSubmitBtn.setBackgroundColor(getResources().getColor(R.color.colorNow));
                    commentSubmitBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                    commentSubmitBtn.setOnClickListener(commentSubmitObj());
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        configureToolbar();

    }

    private View.OnClickListener commentSubmitObj(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the comment and attach that to the user and the comment
                Comments comments = new Comments();

                comments.setMessage(etcommentMessage.getText().toString());
                comments.setUser(user);

                comments.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        // When Now add to the relation
                        ParseRelation<Comments> CurrentComment;
                        CurrentComment = charity.getRelation("UserComments");
                        CurrentComment.add(comments);
                        charity.increment("CommentsNum", 1);
                        charity.saveInBackground();

                    }
                });




                //Pops back the fragment if you cancel
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();




            }
        };

    }


    protected void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextSize(24);
        toolbarTitle.setText("Comment");

        toolbar.setNavigationIcon(R.drawable.ic_x);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pops back the fragment if you cancel
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

    }


}
