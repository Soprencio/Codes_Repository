#include<stdio.h>
#include<stdlib.h>
#include<time.h>

void mostrarMat(int**);
void sumaMat(int**);

int **crearMat(int,int);
void cargaMat(int **,int,int);

int main(void)
{
    int **mat;
    mat = crearMat(5,3);
    cargaMat(mat,4,2);
    mostrarMat(mat);
    sumaMat(mat);
    printf("\n");
    mostrarMat(mat);
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

void mostrarMat(int **mat)
{
    int aux = 0, aux2 = 2;
    for(int fil = 0; fil<5; fil++)
    {
        for(int col = 0; col<3; col++)
        {
            printf("%i ",mat[fil][col]);
            aux = mat[fil][col];
            while(aux>9)
            {
                aux /= 10;
                aux2-=1;
            }
            for(int cont = 0; cont<=aux2; cont++)
            {
                printf(" ");
            }
            aux2 = 2;
        }
        printf("\n");
    }
    printf("\n");
}

void sumaMat(int **mat)
{
    int suma=0, suma_tot=0;
    for(int cont = 0; cont<4; cont++)
    {
        for(int cont2 = 0; cont2<2; cont2++)
        {
            suma+=mat[cont][cont2];
            suma_tot+=mat[cont][cont2];
        }
        mat[cont][2]=suma;
        suma = 0;
    }
    suma = 0;
    mat[4][2]=suma_tot;
    for(int cont = 0; cont<2; cont++)
    {
        for(int cont2 = 0; cont2<4; cont2++)
        {
                suma+=mat[cont2][cont];
        }
        mat[4][cont]=suma;
        suma=0;
    }
}