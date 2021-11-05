package me.ajiew.jithub.data.service

/**
 *
 * @author aJIEw
 * Created on: 2021/5/28 15:33
 */
object ServiceResolver {

    val trendingService by lazy {
        NetworkCreator.createBaseTrending(TrendingService::class.java)
    }

    val loginService by lazy {
        NetworkCreator.createBaseGithub(LoginService::class.java)
    }

    val userService by lazy {
        NetworkCreator.createBaseGithubApi(UserService::class.java)
    }
}