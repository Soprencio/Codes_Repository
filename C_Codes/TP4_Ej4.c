#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int **crearMat(int,int);

void cargaMat(int **,int,int);
void mostrarMat(int**);
void ordenaMat(int**);

int main(void)
{
    int **mat;
    mat=crearMat(5,5);
    cargaMat(mat,5,5);
    mostrarMat(mat);
    ordenaMat(mat);
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
    int aux = 0, aux2 = 3;
    for(int fil = 0; fil<5; fil++)
    {
        for(int col = 0; col<5; col++)
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
            aux2 = 3;
        }
        printf("\n");
    }
    printf("\n");
}

void ordenaMat(int **m)
{
    int aux = 0;
    int vec[25];
    for(int fil = 0; fil<5; fil++)
    {
        for(int col = 0; col<5; col++)
        {
            vec[col+(fil*5)] = m[fil][col];
        }
    }
    for(int i = 0; i<24; i ++)
    {
        for(int j = i+1; j<25; j++)
        {
            if(vec[i]>vec[j])
            {
                aux = vec[i];
                vec[i] = vec[j];
                vec[j] = aux;
            }
        }
    }
    int cont1 = 0;
    int max = 4, min = 0;
    for(int cont = 0; cont<2; cont++)
    {
        for(int col = min; col<max; col++)
        {
            m[min][col] = vec[cont1];
            cont1++;
        }

        for(int fil = min; fil<max; fil++)
        {
            m[fil][max] = vec[cont1];
            cont1++;
        }

        for(int col = max; col>min; col--)
        {
            m[max][col] = vec[cont1];
            cont1++;
        }

        for(int fil = max; fil>min; fil--)
        {
            m[fil][min] = vec[cont1];
            cont1++;
        }
        max -= 1;
        min +=1;
    }
    m[2][2]=vec[cont1];
    printf("Matriz ordenada en espiral: \n");
    mostrarMat(m);
}
