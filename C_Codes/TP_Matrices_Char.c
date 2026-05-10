#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<time.h>

int **crearMat(int, int);
void cargaMat(int **, int, int);
void mostrarMat(int **, int, int);
void busca_ceros(int **, int, int);

int main(void)
{
    srand(time(NULL));
    int **m;
    m = crearMat(6,6);
    cargaMat(m,6,6);
    mostrarMat(m,6,6);
    busca_ceros(m,0,0);
}

int ** crearMat(int f, int c)
{
    int **m = (int**) malloc(sizeof(int**)*f);
    for(int cont = 0; cont<f; cont++)
    {
        m[cont] = (int*) malloc(sizeof(int*)*c);
    }
    return(m);
}

void cargaMat(int **m, int f, int c)
{
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            m[fil][col] = rand() % 2;
        }
    }
    m[0][0] = 0;
}

void mostrarMat(int **m, int f, int c)
{
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            printf("%i ",m[fil][col]);
        }
        printf("\n");
    }

}

void busca_ceros(int **m, int x, int y)
{
    if(x!=5)
    {
        m[x][y] = 3;
        if(x>0 && m[x-1][y]==0)
        {
            busca_ceros(m,x-1,y);
        }
        if(x>0 && y<5 && m[x-1][y+1]==0)
        {
            busca_ceros(m,x-1,y+1);
        }
        if(y<5 && m[x][y+1]==0)
        {
            busca_ceros(m,x,y+1);
        }
        if(x<5 && y<5 && m[x+1][y+1]==0)
        {
            busca_ceros(m,x+1,y+1);
        }
        if(x<5 && m[x+1][y]==0)
        {
            busca_ceros(m,x+1,y);
        }
        if(x<5 && y>0 && m[x+1][y-1]==0)
        {
            busca_ceros(m,x+1,y-1);
        }
        if(y>0 && m[x][y-1]==0)
        {
            busca_ceros(m,x,y-1);
        }
        if(y>0 && x>0 && m[x-1][y-1]==0)
        {
            busca_ceros(m,x-1,y-1);
        }
    }
}