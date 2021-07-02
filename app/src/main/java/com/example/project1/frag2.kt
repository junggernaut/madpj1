package com.example.project1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment


class frag2 : Fragment() {

    var modalList = ArrayList<Modal>()
    var images = intArrayOf(R.drawable.sample_images_01,
        R.drawable.sample_images_02,
        R.drawable.sample_images_03,
        R.drawable.sample_images_04,
        R.drawable.sample_images_05,
        R.drawable.sample_images_06,
        R.drawable.sample_images_07,
        R.drawable.sample_images_08,
        R.drawable.sample_images_09,
        R.drawable.sample_images_10,
        R.drawable.sample_images_11,
        R.drawable.sample_images_12,)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        for(i in images.indices){
            modalList.add(Modal(images[i]))
        }

        var view = inflater.inflate(R.layout.fragment_frag2, container, false)
        var customAdapter = frag2.CustomAdapter(modalList, requireContext());
        var gridView = view.findViewById<GridView>(R.id.gridView);
        gridView.adapter = customAdapter;

        return view
    }

    internal fun newInstant() : frag2
    {
        val args = Bundle()
        val frag = frag2()
        frag.arguments = args
        return frag
    }

    class CustomAdapter(
        var itemModel: ArrayList<Modal>,
        var context: Context
    ) : BaseAdapter(){

        var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        override fun getCount(): Int {
            return itemModel.size
        }

        override fun getItem(p0: Int): Any {
            return itemModel[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            var view = view;
            if(view==null){
                view = layoutInflater.inflate(R.layout.row_items,viewGroup,false);
            }
            var imageView = view?.findViewById<ImageView>(R.id.imageView);
            imageView?.setImageResource(itemModel[position].image!!)

            return view!!;
        }

    }

}