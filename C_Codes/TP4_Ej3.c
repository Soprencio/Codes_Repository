#include<stdio.h>
#include<stdlib.h>
#include<time.h>

void mostrarMat(int**, int**);
void mulMat(int**, int**,int[2][2]);

int **crearMat(int,int);
void cargaMat(int **,int,int);

int main(void)
{
    int **mat;
    int **mat2;
    mat = crearMat(2,3);
    mat2 = crearMat(3,2);
    printf("Matriz 1: \n");
    cargaMat(mat,2,3);
    printf("Matriz 2: \n");
    cargaMat(mat2,3,2);
    int mat_mul[2][2]={{0,0},{0,0}};
    mostrarMat(mat,mat2);
    mulMat(mat,mat2,mat_mul);
    
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



void mulMat(int **mat, int **mat2, int mat_mul[2][2])
{
    for(int cont = 0; cont<2; cont++)
    {
        for(int cont2 = 0; cont2<2; cont2++)
        {
            for(int cont3 = 0; cont3<3; cont3++)
            {
                mat_mul[cont][cont2]+=(mat[cont][cont3]*mat2[cont3][cont2]);
            }
        }
    }
    int aux = 0, aux2 = 4;
    for(int fil = 0; fil<2; fil++)
    {
        for(int col = 0; col<2; col++)
        {
            printf("%i ",mat_mul[fil][col]);
            aux = mat_mul[fil][col];
            while(aux>9)
            {
                aux /= 10;
                aux2-=1;
            }
            for(int cont = 0; cont<=aux2; cont++)
            {
                printf(" ");
            }
            aux2 = 4;
        }
        printf("\n");
    }
}

void mostrarMat(int **mat, int **mat2)
{
    int aux = 0, aux2 = 2;
    for(int fil = 0; fil<2; fil++)
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
    aux = 0; 
    aux2 = 2;
    for(int fil = 0; fil<3; fil++)
    {
        for(int col = 0; col<2; col++)
        {
            printf("%i ",mat2[fil][col]);
            aux = mat2[fil][col];
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