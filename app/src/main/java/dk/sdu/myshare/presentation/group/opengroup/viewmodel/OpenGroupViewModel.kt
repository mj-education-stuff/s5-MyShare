package dk.sdu.myshare.presentation.group.opengroup.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import dk.sdu.myshare.business.model.fakecall.FakecallData
import dk.sdu.myshare.business.model.fakecall.FakecallRepository
import dk.sdu.myshare.business.model.group.GroupData
import dk.sdu.myshare.business.model.group.GroupRepository
import dk.sdu.myshare.business.model.user.UserData
import dk.sdu.myshare.business.model.user.UserRepository
import dk.sdu.myshare.business.utility.ColorGenerator
import dk.sdu.myshare.business.utility.ProfileFormatter
import dk.sdu.myshare.business.utility.ViewModelFactory
import dk.sdu.myshare.presentation.group.opengroup.view.OpenGroupView
import kotlinx.coroutines.launch

class OpenGroupViewModel(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val openGroupId: Int,
    private val fakecallRepository: FakecallRepository
) : ViewModel() {
    private val _currentGroupMembers: MutableLiveData<List<UserData>> = MutableLiveData<List<UserData>>(emptyList())
    val currentUsers: LiveData<List<UserData>> = _currentGroupMembers

    private val _currentGroup: MutableLiveData<GroupData> = MutableLiveData<GroupData>()
    val currentGroup: LiveData<GroupData> = _currentGroup

    private val generatedUserColors: MutableMap<Int, Color> = mutableMapOf()

    private val _fakecallData: MutableLiveData<FakecallData> = MutableLiveData<FakecallData>()
    val fakecallData: LiveData<FakecallData> = _fakecallData

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        observeGroupData()
        refreshCurrentGroup()
//        fetchFakedata()
    }

    private fun observeGroupData() {
        currentGroup.observeForever {
            refreshCurrentGroupMembers()
        }
    }

    fun refreshCurrentGroup() {
        val groupDataResult: GroupData? = groupRepository.fetchGroupDataByID(openGroupId)
        groupDataResult?.let {
            _currentGroup.postValue(it)
        }
    }

    fun refreshCurrentGroupMembers() {
        if (currentGroup.value == null) {
            return
        }

        val currentUsers: MutableList<UserData> = mutableListOf()

        currentGroup.value?.members?.forEach { memberID ->
            val userDataResult: UserData? = userRepository.fetchUserById(memberID)
            userDataResult?.let {
                currentUsers.add(it)
            }
        }

        currentUsers.sortBy { it.name }
        _currentGroupMembers.postValue(currentUsers)
    }

    fun getTemporaryUserColor(userID: Int): Color {
        if (generatedUserColors.containsKey(userID)) {
            return generatedUserColors[userID]!! // FIXME: Better way to handle this
        }

        var color: Color
        do {
            color = ColorGenerator.getRandomPastelColor()
        } while (generatedUserColors.containsValue(color))

        generatedUserColors[userID] = color
        return color
    }

    fun getNameInitials(name: String): String {
        return ProfileFormatter.getNameLetters(name)
    }

    fun getCurrentGroupId(): Int {
        return openGroupId
    }

    fun getCurrentUserId(): Int {
        return 1 // FIXME: Hardcoded for now
    }

    fun fetchFakedata() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val fakecallDataResult: FakecallData? = fakecallRepository.getFakecallDataById(1)
                fakecallDataResult?.let {
                    _fakecallData.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _isLoading.postValue(false)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGroupView() {
    val openGroupViewModel: OpenGroupViewModel = ViewModelFactory.getOpenGroupViewModel(1)
    val navController = rememberNavController()
    OpenGroupView(navController, openGroupViewModel)
}