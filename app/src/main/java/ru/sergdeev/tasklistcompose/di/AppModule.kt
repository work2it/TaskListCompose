package ru.sergdeev.tasklistcompose.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.sergdeev.tasklistcompose.EditViewModel
import ru.sergdeev.tasklistcompose.MainViewModel
import ru.sergdeev.tasklistcompose.data.DataItemRepositoryImpl
import ru.sergdeev.tasklistcompose.domain.DataItemRepository
import ru.sergdeev.tasklistcompose.domain.UserCase

val appModule = module {
    single<DataItemRepository> {
        DataItemRepositoryImpl(androidApplication())
    }

    factory<UserCase> {
        UserCase(dataItemRepository = get())
    }

    viewModel<MainViewModel> {
        MainViewModel(userCase = get())
    }

    viewModel<EditViewModel> {
        EditViewModel(userCase = get())
    }


//            viewModel<EditViewModel> {
//                EditViewModel(repository = get())
//            }
}