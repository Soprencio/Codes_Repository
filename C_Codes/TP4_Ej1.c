#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int **crearMat(int,int);
void cargaMat(int **,int,int);
void mostrarMat(int**,int,int);
void matTrasp(int **,int,int);

int main(void)
{
    int **mat;
    mat = crearMat(3,2);
    cargaMat(mat,3,2);
    mostrarMat(mat,3,2);
    matTrasp(mat,3,2);
    return(0);
}

int **crearMat(int fil, int col)
{
    int **m = (int **)malloc(sizeof(int **)*fil);
    for(int cont = 0; cont<fil; cont++)
    {
        m[cont] = (int *)malloc(sizeof(int *)*col);
    }
    return(m);
}

void cargaMat(int **m, int f, int c)
{
    int num;
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            printf("Ingrese el dato %i %i de la matriz: ",fil,col);
            scanf("%i",&num);
            m[fil][col] = num;
        }
    }
}

void mostrarMat(int **mat, int f, int c)
{
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            printf("%i ",mat[fil][col]);
        }
        printf("\n");
    }
    printf("\n");
}

void matTrasp(int **m, int f, int c)
{
    int **mat2;
    mat2 = crearMat(c,f);
    for(int cont1 = 0; cont1<c; cont1++)
    {
        for(int cont2 = 0; cont2<f; cont2++)
        {
            mat2[cont1][cont2] = m[cont2][cont1] ;
        }
    }
    mostrarMat(mat2,2,3);
}