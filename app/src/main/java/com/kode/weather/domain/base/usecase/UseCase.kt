package com.kode.weather.domain.base.usecase

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class UseCase<out Type, in Param> where Type : Any {

    abstract fun run(param: Param): Flow<Result<Type>>

    operator fun invoke(param: Param): Flow<Result<Type>> {
        return run(param).flowOn(Dispatchers.IO) // Выполняем работу в IO
    }
}