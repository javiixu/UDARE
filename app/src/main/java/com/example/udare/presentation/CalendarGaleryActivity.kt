package com.example.udare.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.udare.Adapter.CalendarAdapter
import com.example.udare.Adapter.FotoAdapter
import com.example.udare.R
import com.example.udare.data.model.CalendarData
import com.example.udare.data.model.Challenge
import com.example.udare.data.model.Post
import com.example.udare.data.model.PostData
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.ChallengeRepository
import com.example.udare.data.repositories.Implementations.PostRepository
import com.example.udare.services.interfaces.IChallengeService
import com.example.udare.services.interfaces.IPostService
import com.example.udare.services.interfaces.IUserService
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class CalendarGaleryActivity : AppCompatActivity() {
    @Inject
    lateinit var postService: IPostService

    @Inject
    lateinit var challengeService: IChallengeService

    @Inject
    lateinit var userService: IUserService

    private val _calendarList = MutableLiveData<List<CalendarData>>()
    val calendarList: LiveData<List<CalendarData>> get() = _calendarList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_galery)

        //used for later referencing the id
        val thisUser = UserSingleton.obtenerInstancia().obtenerUsuario()

        val btnGetChallengesLastMonth = findViewById<Button>(R.id.btnGetChallengesLastMonth);
        val btnGetChallengesLastWeek = findViewById<Button>(R.id.btnGetChallengesLastWeek);
        val btnBackFromCalendar = findViewById<ImageButton>(R.id.btnBackFromCalendar)


        val btnGetChallengesSport = findViewById<Button>(R.id.btnGetChallengesSport);
        val btnGetChallengesCulture = findViewById<Button>(R.id.btnGetChallengesCulture);
        val btnGetChallengesGrowth = findViewById<Button>(R.id.btnGetChallengesGrowth);
        val btnGetChallengesCooking = findViewById<Button>(R.id.btnGetChallengesCooking);
        val btnGetChallengesSocial = findViewById<Button>(R.id.btnGetChallengesSocial);
        val btnGetChallengesAll = findViewById<Button>(R.id.btnGetChallengesAll)


        btnBackFromCalendar.setOnClickListener(){
            Intent(this, Inicio::class.java).also{
                startActivity(it)
            }
        }


        //TODO Block Clicking at the start to give db time to fetch all the info

        //list we will populate with all the challenges by this user, can later be filtered
        var calendarItemList: MutableList<CalendarData> = mutableListOf()

        // recycler view we use here
        val rvCalendar = findViewById<View>(R.id.rvCalendar) as RecyclerView

        postService.getAllPosts(object : PostRepository.callbackGetAllPosts {
            override fun onSuccess(posts: MutableList<Post>) {

                Log.d("tag-calendar","Gotten All posts")
                for(post in posts){
                    //create a new calendar item if the post was by the current user
                    if (post.userID == thisUser.id) {
                        challengeService.getChallengeById(
                            post.challengeID,
                            object : ChallengeRepository.callbackGetChallengeById {
                                override fun onSuccess(challenge: Challenge) {


                                    var orientation = post.orientation
                                    Log.d("tag-caledar","Orientation: $orientation")


                                    //create a new calendar Data Item
                                    val calendarItem = CalendarData(post, challenge, orientation)

                                    calendarItemList.add(calendarItem)
                                    updatePostList(calendarItemList)


                                }

                                override fun onError(mensajeError: String?) {
                                    Log.d("tag-calendar", "Error in getChallengeById" + mensajeError)
                                }
                            })
                    }
                }


                applyChangesRecyclerView(rvCalendar)
            }

            override fun onError(mensajeError: String?) {
                Log.d("tag-calendar","Error in getAllPosts" + mensajeError)
            }



            })

        //BUTTONS for all the categories
        //////////////////////////////////
        btnGetChallengesSocial.setOnClickListener(){
            //filter the calendar list for the social ietms
            var filteredCalendarList = calendarItemList.filter{it.challenge.category.equals("social")}.toMutableList()

            //update the list
            updatePostList(filteredCalendarList)

            //apply all the changes to the recycler view
            applyChangesRecyclerView(rvCalendar)
        }

        btnGetChallengesGrowth.setOnClickListener(){
            //filter the calendar list for the social ietms
            var filteredCalendarList = calendarItemList.filter{it.challenge.category.equals("crecimientopersonal")}.toMutableList()

            //update the list
            updatePostList(filteredCalendarList)

            //apply all the changes to the recycler view
            applyChangesRecyclerView(rvCalendar)
        }


        btnGetChallengesCulture.setOnClickListener(){
            //filter the calendar list for the social ietms
            var filteredCalendarList = calendarItemList.filter{it.challenge.category.equals("cultura")}.toMutableList()

            //update the list
            updatePostList(filteredCalendarList)

            //apply all the changes to the recycler view
            applyChangesRecyclerView(rvCalendar)
        }


        btnGetChallengesSport.setOnClickListener(){
            //filter the calendar list for the social ietms
            var filteredCalendarList = calendarItemList.filter{it.challenge.category.equals("deportes")}.toMutableList()

            //update the list
            updatePostList(filteredCalendarList)

            //apply all the changes to the recycler view
            applyChangesRecyclerView(rvCalendar)
        }

        btnGetChallengesCooking.setOnClickListener(){
            //filter the calendar list for the social ietms
            var filteredCalendarList = calendarItemList.filter{it.challenge.category.equals("cocina")}.toMutableList()

            //update the list
            updatePostList(filteredCalendarList)

            //apply all the changes to the recycler view
            applyChangesRecyclerView(rvCalendar)
        }

        //BUTTONS FOR last month and last week
        //////////////
        btnGetChallengesLastMonth.setOnClickListener(){
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH,-1)

            var oneMonthAgoDate : Date = cal.time




            //filter the calendar list for the social ietms
            var filteredCalendarList = calendarItemList.filter{(it.post.date).equals(oneMonthAgoDate) or (it.post.date).after(oneMonthAgoDate)}.toMutableList()

            //update the list
            updatePostList(filteredCalendarList)

            //apply all the changes to the recycler view
            applyChangesRecyclerView(rvCalendar)
        }


        btnGetChallengesLastWeek.setOnClickListener(){
            val cal = Calendar.getInstance()
            cal.add(Calendar.WEEK_OF_YEAR,-1)
            var oneWeekAgoDate : Date = cal.time


            //filter the calendar list for the social ietms
            var filteredCalendarList = calendarItemList.filter{(it.post.date).equals(oneWeekAgoDate) or (it.post.date).after(oneWeekAgoDate)}.toMutableList()

            //update the list
            updatePostList(filteredCalendarList)

            //apply all the changes to the recycler view
            applyChangesRecyclerView(rvCalendar)
        }


        btnGetChallengesAll.setOnClickListener(){
            updatePostList(calendarItemList)
            applyChangesRecyclerView(rvCalendar)
        }








    }





    fun applyChangesRecyclerView(rvCalendar: RecyclerView){
        calendarList.observe(this@CalendarGaleryActivity, Observer { updatedList ->
            // Actualiza tu RecyclerView o cualquier otra vista aquí con la nueva lista
            rvCalendar.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            //initially set all the completed challenges in the image view sorted by date
            var mutableUpdatedList = updatedList.toMutableList()
            mutableUpdatedList.sortByDescending { it.post.date }
            for (cItem in mutableUpdatedList){
                Log.d("tag-date-sort","Date: ${cItem.post.date}")
            }

            // Attach the adapter to the recyclerview to populate items
            rvCalendar.adapter = CalendarAdapter(mutableUpdatedList)
        })
    }


    fun updatePostList(newList: List<CalendarData>) {
        _calendarList.value = newList
    }





}