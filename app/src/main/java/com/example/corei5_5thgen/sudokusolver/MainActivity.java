package com.example.corei5_5thgen.sudokusolver;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int maxSize=9;
    Context context;
    TextView iboard[][]=new TextView[maxSize][maxSize];
    TextView numpanel[]=new TextView[maxSize+1];
    int board[][] = new int[maxSize][maxSize];
    String numbers[]=new String[maxSize+1];
    LinearLayout sudokuBoard,linimPanel;
    Button set,reset,solve;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNumbers();
        context = this;
        designBoard();
        designNumberPanel();
        /*set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set();
            }
        });*/
        if(flag==0) {
            Set();
        }
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Solve(0,0);
                flag=1;
            }
        });
    }

    private boolean Solve(int x,int y) {
        if(x==9&&y==9)
            return true;//All Done

        if(x<9&&y<9&&emptyGrid(x,y)){
            for(int i=1;i<=maxSize;i++){
                if(checkRow(x,i)&&checkCol(y,i)&&checkSquare(x-x%3,y-y%3,i)){
                    changeGrid(x,y,i);
                    if(x<9&&Solve(x+1,y))
                        return true;
                    else if(x==9){
                        x=0;
                        if(Solve(x,y+1))
                            return true;
                    }
                    changeGrid(x,y,0);
                }
            }
        }else{
            if(x<9&&Solve(x + 1, y))//Move to next row
                return true;
            else if(x==9){//if last row
                x=0;//set row to zero
                if(Solve(x, y + 1))//move to next column
                    return true;
            }
        }
        return false;
    }

    private void changeGrid(int x, int y,int num) {
        board[x][y]=num;
        if(num==0)
            iboard[x][y].setText(" ");
        else
            iboard[x][y].setText(numbers[num-1]);
    }

    private boolean checkSquare(int startRow, int startCol, int num) {
        for(int i=startRow;i<startRow+3;i++){
            for(int j=startCol;j<startCol+3;j++){
                if(board[i][j]==num)//if number exists in the square
                    return false;
            }
        }
        return true;
    }

    private boolean checkCol(int col, int num) {
        for(int i=0;i<maxSize;i++){
            if(board[i][col]==num)
                return false;
        }
        return true;
    }

    private boolean checkRow(int row, int num) {
        for(int i=0;i<maxSize;i++){
            if(board[row][i]==num){
                return false;
            }
        }
        return true;
    }

    private boolean emptyGrid(int x, int y) {
        return board[x][y]==0?true:false;
    }

    private void Set() {
        for(int i=0;i<maxSize;i++){
            for(int j=0;j<maxSize;j++){
                final int x=i;
                final int y=j;
                iboard[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boxClicked(x,y);
                    }
                });
            }
        }
    }

    private void boxClicked(final int row,final int col) {
        linimPanel.setVisibility(View.VISIBLE);
        for(int i=0;i<=maxSize;i++){
            final int temp=i;
            numpanel[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(temp==maxSize) {
                        board[row][col] = 0;
                        iboard[row][col].setTextColor(Color.BLUE);
                        iboard[row][col].setText(" ");
                        linimPanel.setVisibility(View.INVISIBLE);
                    }else {
                        if(!checkRow(row,temp+1)||!checkCol(col,temp+1)||!checkSquare(row-row%3,col-col%3,temp+1)) {
                            Toast.makeText(context, "Wrong Input", Toast.LENGTH_LONG).show();
                        }
                        else {
                            board[row][col] = temp + 1;
                            iboard[row][col].setTextColor(Color.RED);
                            iboard[row][col].setText(numbers[temp]);
                        }
                        linimPanel.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    private void initNumbers() {
        //set= (Button) findViewById(R.id.set);
        reset= (Button) findViewById(R.id.reset);
        solve= (Button) findViewById(R.id.solve);
        numbers[0]="1";
        numbers[1]="2";
        numbers[2]="3";
        numbers[3]="4";
        numbers[4]="5";
        numbers[5]="6";
        numbers[6]="7";
        numbers[7]="8";
        numbers[8]="9";
        numbers[9]="<X";
    }

    private void designNumberPanel() {
        int sizeofCell=Math.round(ScreenWidth()/(maxSize+1));
        LinearLayout.LayoutParams singleRow =new LinearLayout.LayoutParams(sizeofCell*(maxSize+1),sizeofCell);
        LinearLayout.LayoutParams singleCell= new LinearLayout.LayoutParams(sizeofCell,sizeofCell);
        linimPanel=(LinearLayout)findViewById(R.id.numberPanel);
        linimPanel.setVisibility(View.INVISIBLE);
            LinearLayout linRow=new LinearLayout(context);
            for(int j=0;j<=maxSize;j++){
                numpanel[j]=new TextView(context);
                numpanel[j].setBackgroundResource(R.drawable.cell);
                //numpanel[i][j].setText((i * 6 + j));
                numpanel[j].setText(numbers[j]);
                numpanel[j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                numpanel[j].setTextColor(Color.WHITE);
                numpanel[j].setTextSize(25);
                linRow.addView(numpanel[j],singleCell);
            }
            linimPanel.addView(linRow,singleRow);

    }

    private void designBoard() {
        int sizeofCell=Math.round(ScreenWidth()/maxSize);
        LinearLayout.LayoutParams singleRow =new LinearLayout.LayoutParams(sizeofCell*maxSize,sizeofCell);
        LinearLayout.LayoutParams singleCell= new LinearLayout.LayoutParams(sizeofCell,sizeofCell);
        sudokuBoard= (LinearLayout) findViewById(R.id.sudokuBoard);

        for(int i=0;i<maxSize;i++){
            LinearLayout linRow = new LinearLayout(context);
            for(int j=0;j<maxSize;j++){
                board[i][j]=0;
                iboard[i][j]=new TextView(context);
                if(((i>=3&&i<=5)&&!(j>=3&&j<=5))||(!(i>=3&&i<=5)&&(j>=3&&j<=5))) {
                    iboard[i][j].setBackgroundResource(R.drawable.cell2);
                }else{
                    iboard[i][j].setBackgroundResource(R.drawable.cell1);
                }
                linRow.addView(iboard[i][j], singleCell);
                iboard[i][j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                iboard[i][j].setTextColor(Color.BLUE);
                iboard[i][j].setTextSize(25);
            }
            sudokuBoard.addView(linRow, singleRow);
        }
    }

    private float ScreenWidth() {
        Resources resources=context.getResources();
        DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

}
