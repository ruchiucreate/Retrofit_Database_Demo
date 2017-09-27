package com.demo.retrofit_database_demo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.abc.greendaoexample.db.DaoMaster;
import com.abc.greendaoexample.db.DaoSession;
import com.abc.greendaoexample.db.Movie;
import com.abc.greendaoexample.db.MovieDao;
import com.demo.retrofit_database_demo.R;
import com.demo.retrofit_database_demo.adapter.UserRecyclerAdapter;
import com.demo.retrofit_database_demo.models.MovieResponseBean;
import com.demo.retrofit_database_demo.retrofit.ApiClient;
import com.demo.retrofit_database_demo.retrofit.ApiInterface;
import com.demo.retrofit_database_demo.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_user)
    RecyclerView rvUser;
    @BindView(R.id.tv_no_data)
    TextView tvNoRecordFound;
    RecyclerView.LayoutManager mLayoutManager;
    UserRecyclerAdapter mAdapter;

    ProgressDialog mDialog;
    Context mContext;

    private MovieDao movieDao;
    private final String DB_NAME = "my-app-db";

    MovieResponseBean.Result mDataBean = new MovieResponseBean.Result();
    List<MovieResponseBean.Result> mUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        movieDao = setupDb();

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(getString(R.string.please_wait_text));
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        if (CommonMethods.isConnectedToInternet(mContext)) {
            hitMovieDetailWebservice();
        } else {
            CommonMethods.makeToast(mContext, getString(R.string.no_internet_text));
            generateResult();
        }

    }

    private void generateResult() {
        List<Movie> movieList = getFromSQL();
        int size = movieList.size();
        mDialog.dismiss();
        if (size > 0) {
            mUserList.clear();

            for (int i = 0; i < size; i++) {
                Movie currentItem = movieList.get(i);
                mDataBean.setTitle(currentItem.getTitle());
                mDataBean.setReleaseDate(currentItem.getRelease_date());
                mDataBean.setOverview(currentItem.getDescription());
                mUserList.add(mDataBean);
            }
            initializingRecyclerView(mUserList);
        } else {
            CommonMethods.makeToast(mContext, getString(R.string.no_user_in_db_text));
        }
    }

    //---------------------------------SQL QUERY Functions-----------------------------------------//
    public List<Movie> getFromSQL() {
        List<Movie> userses = movieDao.queryBuilder().orderAsc(MovieDao.Properties.Id).build().list();
        return userses;
    }

    public void SaveToSQL(Movie movie) {
        movieDao.insert(movie);
    }
    //----------------------------***END SQL QUERY***---------------------------------------------//


    //-------------------------------DB Setup Functions---------------------------------------------//

    //Return the Configured LogDao Object
    public MovieDao setupDb() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        DaoMaster master = new DaoMaster(db);//create masterDao
        DaoSession masterSession = master.newSession(); //Creates Session session
        return masterSession.getMovieDao();
    }
    //-------------------------***END DB setup Functions***---------------------------------------//

    private void initializingRecyclerView(List<MovieResponseBean.Result> mDataList) {
        mLayoutManager = new LinearLayoutManager(this);
        rvUser.setLayoutManager(mLayoutManager);

        mAdapter = new UserRecyclerAdapter(this, mDataList);
        rvUser.setAdapter(mAdapter);
    }

    private void hitMovieDetailWebservice() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Call<MovieResponseBean> getUserDetailService = apiInterface.getMovieDetailService("75f6424e7a29a77b165919266ebc82a7");
        getUserDetailService.enqueue(new Callback<MovieResponseBean>() {
            @Override
            public void onResponse(Call<MovieResponseBean> call, Response<MovieResponseBean> response) {
                mDialog.dismiss();
                MovieResponseBean mResponse = response.body();
                try {
                    initializingRecyclerView(mResponse.getResults());
                    CommonMethods.makeToast(mContext, getString(R.string.success_text));
                    saveDataToDB(mResponse.getResults());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    CommonMethods.makeToast(mContext, getString(R.string.error_occur_text));
                }
            }

            @Override
            public void onFailure(Call<MovieResponseBean> call, Throwable t) {
                mDialog.dismiss();
                CommonMethods.makeToast(mContext, getString(R.string.error_occur_text));
            }
        });
    }

    private void saveDataToDB(List<MovieResponseBean.Result> mData) {
        if (mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                Movie movie = new Movie();
                movie.setTitle(mData.get(i).getTitle());
                movie.setRelease_date(mData.get(i).getReleaseDate());
                movie.setDescription(mData.get(i).getOverview());
                SaveToSQL(movie);
            }
        }
    }
}
