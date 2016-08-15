package com.view.coustomrequire;

public class MyAdapter extends RecyclerView.Adapter implements ItemHelpter.Callback{
    private Context context;
    private LayoutInflater mInflater;
    private RecyclerView mRecycler;

    public MyAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SwipeLayout getSwipLayout(float x, float y) {
        return (SwipeLayout)mRecycler.findChildViewUnder(x,y);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycler = recyclerView;
        recyclerView.addOnItemTouchListener(new ItemHelpter(context,this));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder)holder;

        holder1.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "测试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        SwipeLayout root;
        ImageView imageView;
        RelativeLayout mLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (SwipeLayout)itemView;
            imageView = (ImageView)itemView.findViewById(R.id.image_test);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.layout_item);
        }
    }

}