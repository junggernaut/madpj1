package com.example.project1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class frag1 : Fragment() {

    class CustomAdapter(private val dataSet: List<ContactDTO>, ctx: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        private var context: Context = ctx
        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView
            val number: TextView
            val profile: ImageView

            init {
                // Define click listener for the ViewHolder's View.
                name = view.findViewById(R.id.tv_name)
                number = view.findViewById(R.id.tv_number)
                profile = view.findViewById(R.id.iv_profile)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.contact_child, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.name.text = dataSet[position].name
            viewHolder.number.text = dataSet[position].number
            if(dataSet[position].image != null) {
                viewHolder.profile.setImageBitmap(dataSet[position].image)
            }
            else {
                viewHolder.profile.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher_round))
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size

    }



    fun getContacts(view: View) {
        //TODO get contacts code here
        var contactList : MutableList<ContactDTO> = ArrayList()
        val contacts = requireActivity().contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        while(contacts!!.moveToNext()) {
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = ContactDTO()
            obj.name = name
            obj.number = number

            val photo_uri = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
            if(photo_uri != null) {
                obj.image = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, Uri.parse(photo_uri))
            }
            else {
                val bitmap = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_account_circle_24)!!.toBitmap()
                obj.image = bitmap
            }

            contactList.add(obj)
        }
        view.findViewById<RecyclerView>(R.id.contact_list).adapter = CustomAdapter(contactList, view.context)
        contacts!!.close()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view:View = inflater.inflate(R.layout.fragment_frag1, container, false)
        view.findViewById<RecyclerView>(R.id.contact_list).layoutManager = LinearLayoutManager(view.context)

        while(ContextCompat.checkSelfPermission(view.context, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){

        }
        getContacts(view)
        view.findViewById<FloatingActionButton>(R.id.add_button).setOnClickListener {
            val intent = Intent(requireActivity(), addContact::class.java)
            startActivity(intent)
        }
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            getContacts(view)
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }



    internal fun newInstant() : frag1
    {
        val args = Bundle()
        val frag = frag1()
        frag.arguments = args
        return frag
    }
}