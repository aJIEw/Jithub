package me.ajiew.jithub.ui.home

import me.ajiew.core.base.BaseFragment
import me.ajiew.core.base.repository.IRepository
import me.ajiew.core.base.viewmodel.BaseViewModel
import me.ajiew.jithub.BR
import me.ajiew.jithub.R
import me.ajiew.jithub.databinding.FragmentTestBinding

/**
 *
 * @author aJIEw
 * Created on: 2021/11/1 16:55
 */
class TestRepository : IRepository

class TestViewModel : BaseViewModel<TestRepository>()

class TestFragment : BaseFragment<FragmentTestBinding, TestViewModel>() {
    override val layoutId: Int = R.layout.fragment_test
    override val viewModelId: Int = BR.vm
    override val viewModel: TestViewModel = TestViewModel()
}