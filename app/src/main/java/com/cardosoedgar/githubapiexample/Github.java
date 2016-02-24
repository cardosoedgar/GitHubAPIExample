package com.cardosoedgar.githubapiexample;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by edgarcardoso on 2/24/16.
 */
public interface Github {

    @GET("users/{user}")
    Observable<User> getUser(@Path("user") String username);

    @GET("repos/{user}/{repo}/contributors")
    Observable<List<Contributor>> getContributors(@Path("user") String username, @Path("repo") String repo, @Query("page") int pageNumber);
}
