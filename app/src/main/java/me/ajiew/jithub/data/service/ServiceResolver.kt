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

    val userService by lazy {
        NetworkCreator.createBaseGithub(UserService::class.java)
    }
}