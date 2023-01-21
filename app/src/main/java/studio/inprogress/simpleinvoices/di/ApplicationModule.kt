package studio.inprogress.simpleinvoices.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import studio.inprogress.simpleinvoices.BuildConfig
import studio.inprogress.simpleinvoices.api.InvoiceApiService
import studio.inprogress.simpleinvoices.util.TimberLoggingInterceptor
import studio.inprogress.simpleinvoices.util.Tls12SocketFactory.Companion.enableTls12
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideOkHttpBuilder(
        @ApplicationContext context: Context,
        interceptor: Interceptor
    ): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder().enableTls12()

        if (BuildConfig.DEBUG) {
            builder.interceptors().add(interceptor)
        }

        return builder
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BuildConfig.API_HOST)
        .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): Interceptor = TimberLoggingInterceptor()

    @Singleton
    @Provides
    fun provideInvoicesApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpBuilder: OkHttpClient.Builder
    ): InvoiceApiService = retrofitBuilder.client(okHttpBuilder.build())
        .build().create(InvoiceApiService::class.java)
}
