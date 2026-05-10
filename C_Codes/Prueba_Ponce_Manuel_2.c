#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<string.h>

char** crearMat(int, int);
void mostrarMat(char**, int, int, char*);
void cargaMat(char**, int, int);
void comprarAsiento(char**, int, int, char*);
void montoVenta(char**, int, int);

int main(void)
{
    char **mat;
    mat = crearMat(25,6);
    cargaMat(mat,25,6);

    char* vec = malloc(sizeof(char*)*6);
    vec[0] = 'A';
    vec[1] = 'B';
    vec[2] = 'C';
    vec[3] = 'H';
    vec[4] = 'J';
    vec[5] = 'K';

    int aux = 0, num;
    while(aux == 0)
    {
        printf("Elija que opción quiere realizar \n 1) Comprar Asiento \n 2) Mostrar Disponibilidad \n 3) Mostrar monto de ventas \n 0) Salir \n Elige:");
        scanf("%i",&num);
        while(num<0 || num>3)
        {
            printf("Elija que opción quiere realizar \n 1) Comprar Asiento \n 2) Mostrar Disponibilidad \n 3) Mostrar monto de ventas \n 0) Salir \n Elige:");
            scanf("%i",&num);
        }
        switch(num)
        {
            case 0:
            {
                aux = 1;
                break;
            }
            case 1:
            {
                comprarAsiento(mat,25,6,vec);
                break;
            }
            case 2:
            {
                printf("\nUn espacio vacío '| |' es DISPONIBLE, una X '|X|' es YA VENDIDO/NO DISPONIBLE\n");
                mostrarMat(mat,25,6,vec);
                break;
            }
            case 3:
            {
                montoVenta(mat,25,6);
                break;
            }
        }
    }
    return(0);
}

char** crearMat(int f, int c)
{
    char** m = (char**)malloc(sizeof(char**)*f);
    for(int cont = 0; cont<f; cont++)
    {
        m[cont] = (char*)malloc(sizeof(char*)*c);
    }
    return(m);
}

void cargaMat(char** m, int f, int c)
{
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            m[fil][col] = ' ';
        }
    }
    m[0][1] = 'X';
    m[0][4] = 'X';
    m[1][1] = 'X';
    m[1][4] = 'X';
}

void mostrarMat(char **m, int f, int c, char* v)
{
    printf("\n  ");
    for(int vec = 0; vec<c; vec++)
    {
        printf(" %c",v[vec]);
    }
    printf("\n");

    int cont = 1;
    for(int fil = 0; fil<f; fil++)
    {
        if(cont>9)
        {
            printf("%i",cont);
        }
        else
        {
            printf("%i ",cont);
        }
        for(int col = 0; col<c; col++)
        {
            printf("|%c",m[fil][col]);
        }
        if(fil<2)
        {
            printf("| \033[33mPreferencial\033[37m\n");
        }
        else if(fil>=2 && fil <8)
        {
            printf("| Grupo \033[34mA\033[37m\n");
        }
        else if(fil>=8 && fil<14)
        {
            printf("| Grupo \033[35mB\033[37m\n");
        }
        else if(fil>= 14 && fil<20)
        {
            printf("| Grupo \033[32mC\033[37m\n");
        }
        else if(fil>=20)
        {
            printf("| Grupo \033[31mD\033[37m\n");
        }
        cont++;
        if(cont==3)
        {
            cont+=2;
        }
    }
}

void comprarAsiento(char** m, int f, int c, char* v)
{
    printf("\nPrecios: \n  Preferencial $200000 \n  Grupo A $100000 \n  Grupo B $70000\n  Grupo C y D $50000");
    mostrarMat(m,f,c,v);
    int num, aux;
    char let;

    printf("Seleccione la fila del asiento (1-27 sin 3 ni 4): ");
    scanf("%i",&num);
    while(num<1 || num == 3 || num == 4 || num > 27)
    {
        printf("Seleccione la fila del asiento (1-27 sin 3 ni 4): ");
        scanf("%i",&num);
    }
    if(num>=5)
    {
        num-=2; //aunque los asientos 3 y 4 no existan, en la matriz si, asi que reducimos el numero en 2 si es 5 o mayor para que cuadre con la matriz.
    }

    printf("Seleccione la columna (A-B-C-H-J-K En mayúscula): ");
    getchar(); //al usar chars, sin poner esto hace un enter automático, entra al While ignorando que pongas. Getchar usa el enter y no lo usa el scanf.
    scanf("%c",&let);
    while(let != 'A' && let != 'B' && let != 'C' && let != 'H' && let != 'J' && let != 'K')
    {
        printf("Seleccione la columna (A-B-C-H-J-K En mayúscula): ");
        scanf("%c",&let);
    }

    for(int vec = 0; vec<c; vec++)
    {
        if(let == v[vec])
        {
            aux = vec;
        }
    }

    if(m[num-1][aux]==' ')
    {
        m[num-1][aux]='X';
        mostrarMat(m,f,c,v);
    }
    else
    {
        printf("Ese asiento no está disponible \n");
    }
}

void montoVenta(char** m, int f, int c)
{
    int grupos=0, total=0;
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            if(m[fil][col]=='X')
            {
                if(fil<2)
                {
                    total+=200000;
                    grupos+=200000;
                }
                else if(fil>=2 && fil<8)
                {
                    total+=100000;
                    grupos+=100000;
                }
                else if(fil>=8 && fil<14)
                {
                    total+=70000;
                    grupos+=70000;
                }
                else if(fil>=14)
                {
                    total+=50000;
                    grupos+=50000;
                }
            }
        }
        if(fil==1)
        {
            grupos-=800000; //asiento que ya venian deshabilitados en clase preferencial
            printf("Clase Preferencial: $%i \n",grupos);
            grupos = 0;
        }
        else if(fil==7)
        {
            printf("Grupo A: $%i \n",grupos);
            grupos = 0;
        }
        else if(fil==13)
        {
            printf("Grupo B: $%i \n",grupos);
            grupos = 0;
        }
        else if(fil==19)
        {
            printf("Grupo C: $%i \n",grupos);
            grupos = 0;
        }
        else if(fil==f-1)
        {
            printf("Grupo D: $%i \n",grupos);
            total -= 800000; //asiento que ya venian deshabilitados en clase preferencial
            printf("Total: $%i \n",total);
        }
    }
}