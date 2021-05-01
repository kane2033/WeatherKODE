package com.kode.weather.domain.base.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class UseCase<out Type, in Param> {

    // В переопределении run нужно только вернуть Type
    abstract fun run(param: Param): Type

    // "invoke" упакует результат run в flow и result
    operator fun invoke(param: Param): Flow<Result<Type>> {
        val flow = flow {
            val result = try {
                Result.success(run(param))
            } catch (e: Throwable) {
                Result.failure(e)
            }
            emit(result)
        }
        return flow.flowOn(Dispatchers.IO)
    }

}