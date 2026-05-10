#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<time.h>

int *crearVec(int);
int cargarVec(int *, int);
void mostrar(int *, int);
void invertido(int *,int);
void cargarCero(int *, int);

int main(void)
{
       int * vec;
    int ext;
    printf("Ingrese el largo de su vector (1-20): ");
    scanf("%i",&ext);
    while(ext<1 || ext>20)
    {
        printf("Ingrese el largo de su vector (1-20): ");
        scanf("%i",&ext);
    }
    vec = crearVec(ext);
    int tam1 = cargarVec(vec,ext);
    mostrar(vec,tam1);
    invertido(vec,tam1);
    return(0);
}

int *crearVec(int i)
{
    int e = i+1;
    int *v = malloc(sizeof(int)*e);
    return(v);
}

int cargarVec(int *v, int i)
{
    int aux = 0, num, cont = 0;
    while(aux==0)
    {
        if(cont<i)
        {
            printf("Ingrese un dato del vector (1-100) (Ingrese 0 para dejar de ingresar): ");
            scanf("%i",&num);
        }
        else
        {
            num = 0;
        }
        while(num>100 || num<0)
        {
            printf("Ingrese un dato entre 1-100 (Ingrese 0 para dejar de ingresar): ");
            scanf("%i",&num);
        }
        if(num>0 && cont<101)
        {
            v[cont] = num;
        }
        else
        {
            aux = 1;
            cont--;
        }
        cont++;
    }
    return(cont);
}

void mostrar(int *v, int i)
{
    for(int cont = 0; cont<i; cont++)
    {
        if(i-cont != 1)
        {
            printf("%i-",v[cont]);
        }
        else
        {
            printf("%i \n", v[cont]);
        }
    }
}

void cargarCero(int *v, int e)
{
    for(int cont = 0; cont<e; cont++)
    {
        v[cont]=0;
    }
}

void invertido(int *v, int i)
{
    int *vec2 = crearVec(i);
    cargarCero(vec2,i);
    int cont_a=0;
    for(int cont = i-1; cont>=0; cont--)
    {
        vec2[cont_a] = v[cont];
        cont_a++;
    }
    mostrar(vec2,i);
}