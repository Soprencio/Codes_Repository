#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<time.h>

char** crearMat(int,int);
void cargaMat(char **, int, int);

void presentarTablero(char **, int, int);
void colocarFicha(char **);
int comprobarGanador(char **, int, int, int);
int iniciarJuego(char**, int*, int, int);

int *crearVec(int);
void cargaVec(int*,int);

int main(void)
{
    char **mat;
    mat = crearMat(6,7);
    cargaMat(mat,6,7);
    presentarTablero(mat,6,7);
    colocarFicha(mat);
    return(0);
}

void colocarFicha(char **mat)
{
    int aux = 0, blanRoj = 0, num, turno = 0;
    int *vec_fil;
    vec_fil = crearVec(7);
    cargaVec(vec_fil,7);

    while(aux == 0)
    {
        if(turno<42)
        {
            printf("Ingrese en que columna (1-7, Ingrese 0 para reiniciar tablero) va a jugar el jugador ");
            if(blanRoj == 0)
            {
                printf("Rojo: ");
                blanRoj = 1;
            }
            else
            {
                printf("Blanco: ");
                    blanRoj = 0;
            }
            scanf("%i",&num);
            while((num <0 || num >7) || vec_fil[num-1] == -1)
            {
                if(vec_fil[num-1] == -1)
                {
                    printf("Columna llena, ");
                }
                printf("Ingrese un número válido (1-7 y 0 para reiniciar tablero)");
                scanf("%i",&num);
            }
            if(num != 0)
            {
                if(blanRoj == 1)
                {
                    mat[vec_fil[num-1]][num-1]= 'R';
                }
                else
                {
                    mat[vec_fil[num-1]][num-1]= 'B';
                }
                vec_fil[num-1]--;
                presentarTablero(mat,6,7);
                aux = comprobarGanador(mat,6,7,blanRoj);
            }
            else
            {
                blanRoj = iniciarJuego(mat,vec_fil,6,7);
            }
            turno++;
        }
        else
        {
            printf("Vaya, a terminado en empate \n");
            aux = 1;
        }
    }
}

int iniciarJuego(char** m, int* v, int f, int c)
{
    int br = 0;
    cargaMat(m,f,c);
    cargaVec(v,c);
    presentarTablero(m,6,7);
    return(br);
}

char** crearMat(int fil, int col)
{
    char **m = (char **)malloc(sizeof(char **)*fil);
    for(int cont = 0; cont<fil; cont++)
    {
        m[cont] = (char *)malloc(sizeof(char *)*col);
    }
    return(m);
}

void cargaMat(char **m, int f, int c)
{
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            m[fil][col] = ' ';
        }
    }
}

void presentarTablero(char **mat, int f, int c)
{
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            if(mat[fil][col]=='R')
            {
                printf("|\x1b[31m%c\x1b[37m",mat[fil][col]);
            }
            else
            {
                printf("|%c",mat[fil][col]);
            }
        }
        printf("|\n");
    }
    printf(" ");
    for(int num = 1; num<=c; num++)
    {
        printf("%i ",num);
    }
    printf("\n");
}

int *crearVec(int f)
{
    int *v = malloc(sizeof(int)*f);
    return(v);
}

void cargaVec(int *v, int f)
{
    for(int cont = 0; cont<f; cont++)
    {
        v[cont] = 5;
    }
}

int comprobarGanador(char **m, int f, int c, int br)
{
    char aux;
    int aux2 = 0;
    if(br == 1)
    {
        aux = 'R';
    }
    else
    {
        aux = 'B';
    }
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            if(m[fil][col]==aux)
            {
                if(col > 2)
                {
                    if(m[fil][col-1] == aux && m[fil][col-2] == aux && m[fil][col-3] == aux)
                    {
                        aux2 = 1;
                    }
                }
                if(col < 4)
                {
                    if(m[fil][col+1] == aux && m[fil][col+2] == aux && m[fil][col+3] == aux)
                    {
                        aux2 = 1;
                    }
                }
                if(fil < 3)
                {
                    if(m[fil+1][col] == aux && m[fil+2][col] == aux && m[fil+3][col] == aux)
                    {
                        aux2 = 1;
                    }
                }
                if(fil > 2)
                {
                    if(m[fil-1][col] == aux && m[fil-2][col] == aux && m[fil-3][col] == aux)
                    {
                        aux2 = 1;
                    }
                }
                if(fil > 2 && col > 2)
                {
                    if(m[fil-1][col-1] == aux && m[fil-2][col-2] == aux && m[fil-3][col-3] == aux)
                    {
                        aux2 = 1;
                    }
                }
                if(fil < 3 && col < 4)
                {
                    if((m[fil+1][col+1] == aux && m[fil+2][col+2] == aux && m[fil+3][col+3] == aux))
                    {
                        aux2 = 1;
                    }
                }

                if(fil > 2 && col < 4)
                {
                    if(m[fil-1][col+1] == aux && m[fil-2][col+2] == aux && m[fil-3][col+3] == aux)
                    {
                        aux2 = 1;
                    }
                }
                
                if(fil < 3 && col > 2)
                {
                    if(m[fil+1][col-1] == aux && m[fil+2][col-2] == aux && m[fil+3][col-3] == aux)
                    {
                        aux2 = 1;
                    }
                }
                if(aux2 == 1)
                {
                    printf("Felicidades, el ganador fue el ");
                    if(br == 1)
                    {
                    printf("Rojo \n");
                    }
                    else
                    {
                        printf("Blanco \n");
                    }
                    return(1);
                }
            }
        }
    }
    return(0);
}