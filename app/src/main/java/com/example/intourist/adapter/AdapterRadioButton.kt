package com.example.intourist.adapter

//class AdapterCategory: RecyclerView.Adapter<AdapterCategory.MyViewHolder>() {
//    private var myList = emptyList<Category>()
//    private var mSelectedItem = -1
//
//    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        fun bindItems(position: Int, selectedPosition: Int) {
//            if ((selectedPosition == -1 && position == 0))
//                itemView.categoryRB.isChecked = true
//            else
//                itemView.categoryRB.isChecked = selectedPosition == position
//
////            itemView.categoryRB.setOnClickListener {
////                mSelectedItem = adapterPosition
////                notifyDataSetChanged()
////            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        return MyViewHolder(LayoutInflater.from(parent.context)
//                .inflate(R.layout.categories, parent, false))
//    }
//
//    private var onItemClickListener: ((Category) -> Unit)? = null
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val current = myList[position]
//        holder.bindItems(position, mSelectedItem)
//        holder.itemView.categoryRB.text = current.name
//        holder.itemView.categoryRB.setOnClickListener {
//            onItemClickListener?.let { it(current) }
//            mSelectedItem = position
//            notifyDataSetChanged()
//        }
//
//    }
//
//    override fun getItemCount(): Int {
//        return myList.size
//    }
//
//    fun setData(newList: List<Category>) {
//        this.myList = newList
//        notifyDataSetChanged()
//    }
//
//    fun setOnItemClickListener(listener: (Category) -> Unit) {
//        onItemClickListener = listener
//    }
//}