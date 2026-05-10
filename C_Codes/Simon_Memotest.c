#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<time.h>
#include<string.h>

char **crearM(int);
void cargaM(char**,int);
void cargaMemo(char**,int, char*);
char *crearVec(int);
void mostrarMat(char**,char*,int);
void posxy(int,int,char*);
void simon(char**,char*,int,int*);
void jugarMemo(char**,char*,int);
void espera();

int main(void)
{
    srand(time(NULL));
    char **matriz;
    char *pos;
    pos = crearVec(15);
    int juego;
    printf("Ingrese que quiere jugar: (0-Simon Says) (1-Memotest): ");
    scanf("%i",&juego);
    system("clear");
    switch(juego)
    {
        case 0:
        {
            //simon comenta
            matriz=crearM(8);
            cargaM(matriz,8);
            mostrarMat(matriz,pos,8);
            int *com = malloc(sizeof(int)*50);
            posxy(4,20,pos);
            printf("%s%s",pos,"\033[1;35mSIMON SAYS\033[0;m");
            simon(matriz,pos,8,com);
            //simon comenta
            break;
        }
        case 1:
        {
            //memotest
            matriz=crearM(4);
            char *aux = malloc(sizeof(char)*16);
            for(int i = 0; i<16; i++)
            {
                aux[i]='9';
            }
            cargaMemo(matriz,4,aux);
            mostrarMat(matriz,pos,4);
            jugarMemo(matriz,pos,4);
            break;
        }
        default:
        {
                printf("ningun juego\n");
        }
    }


}

void mostrarMat(char** m, char*pos,int fil)
{
    for(int f = 0; f<fil; f++)
    {
        for(int c = 0; c<fil; c++)
        {
            posxy(f+1,(c+1)*2,pos);
            if(fil==8)
            {
                printf("%s%c\n",pos,m[f][c]);
            }
            else
            {
                printf("%s%s\n",pos,"x");
                posxy(1,12,pos);
                printf("%s%s",pos,"Parejas encontradas:0");
                posxy(1,36,pos);
                printf("%s%s",pos,"Cantidad de jugadas: ");
            }
        }
    }

}

void jugarMemo(char **m, char*pos, int fil)
{
    int aux = 0, turno = 0;
    int f, c;
    int *aux3 = malloc(sizeof(int)*6);
    aux3[0]=10;
    aux3[2]=11;
    char **mat;
    mat = crearM(fil);
    for(int fi = 0; fi<fil; fi++)
    {
        for(int co = 0; co<fil; co++)
        {
            mat[fi][co] = 9;
        }
    }
    aux3[4] = 1;
    aux3[5] = 0;
    while(aux>=0)
    {
        posxy(1,58,pos);
        printf("%s%i",pos,aux3[5]);
        if(turno == 1)
        {
            aux3[5]++;
        }
        posxy(2,12,pos);
        printf("%s%s\n",pos,"                                 ");
        posxy(6,1,pos);
        printf("%s%s",pos,"Ingrese el número de fila (1-4): ");
        posxy(6,33,pos);
        scanf("%i",&f);
        f=f-1;
        while(f<0 || f>3)
        {
            posxy(3,12,pos);
            printf("%s%s\n",pos,"\033[1;31mIngrese un número entre(1-4)\033[0;m");
            posxy(6,34,pos);
            printf("%s",pos);
            scanf("%i",&f);
            posxy(3,12,pos);
            printf("%s%s",pos,"                                 ");
            f=f-1;
        }
        posxy(7,1,pos);
        printf("%s%s",pos,"Ingrese el número de columna (1-4): ");
        posxy(7,36,pos);
        scanf("%i",&c);
        c=c-1;
        while(c<0 || c>3)
        {
            posxy(3,12,pos);
            printf("%s%s\n",pos,"\033[1;31mIngrese un número entre(1-4)\033[0;m");
            posxy(7,37,pos);
            printf("%s",pos);
            scanf("%i",&c);
            posxy(3,12,pos);
            printf("%s%s",pos,"                                 ");
            c=c-1;
        }
        if(m[f][c]!=mat[f][c])
        {
            aux3[aux] = f;
            aux3[aux+1]=c;
            if(aux3[0]==aux3[2] && aux3[1]==aux3[3])
            {
                posxy(2,12,pos);
                printf("%s%s\n",pos,"\033[1;31mNúmero ya adivinado\033[0;m");
                posxy(6,1,pos);
                printf("%s%s\n",pos,"                                 ");
                posxy(7,1,pos);
                printf("%s%s\n",pos,"                                        ");
                espera(0.9);
            }
            else
            {
                posxy(f+1,(c+1)*2,pos);
                printf("%s%i\n",pos,m[f][c]);
                posxy(6,1,pos);
                printf("%s%s",pos,"                                 ");
                posxy(7,1,pos);
                printf("%s%s",pos,"                                        ");
                if(turno == 0)
                {
                    turno = 1;
                    aux+=2;
                }
                else
                {
                    turno = 0;
                    aux = 0;
                    espera(1.5);
                    if(m[aux3[0]][aux3[1]]!=m[aux3[2]][aux3[3]])
                    {
                        posxy(aux3[0]+1,(aux3[1]+1)*2,pos);
                        printf("%s%s",pos,"x");
                        posxy(aux3[2]+1,(aux3[3]+1)*2,pos);
                        printf("%s%s",pos,"x");
                    }
                    else
                    {
                        posxy(1,32,pos);
                        printf("%s%i",pos,aux3[4]);
                        aux3[4]++;
                        mat[aux3[0]][aux3[1]] = m[aux3[0]][aux3[1]];
                        mat[aux3[2]][aux3[3]] = m[aux3[2]][aux3[3]];
                    }
                }
            }
        }
        else
        {
            posxy(2,12,pos);
            printf("%s%s\n",pos,"\033[1;31mNúmero ya adivinado\033[0;m");
            posxy(6,1,pos);
            printf("%s%s\n",pos,"                                 ");
            posxy(7,1,pos);
            printf("%s%s\n",pos,"                                        ");
            espera(0.9);
        }
        if(aux3[4]==9)
        {
            posxy(1,58,pos);
            printf("%s%i",pos,aux3[5]);
            posxy(2,12,pos);
            printf("%s%s\n",pos,"\033[1;32mFelicidades, has ganado\033[0;m");
            posxy(6,1,pos);
            printf("%s%s\n",pos," ");
            aux=-1;
        }
    }
}

void simon(char **m, char*pos, int fil, int *com)
{
    int cont = 1;
    int num, aux = 0;
    float tem = 1.3;
    while(aux == 0)
    {
        num = rand() % 4;
        com[cont-1] = num;
        for(int sim = 0; sim<cont; sim++)
        {
            for(int f = 0; f<4; f++)
            {
                for(int c = 0; c<4; c++)
                {
                    if(com[sim] == 0)
                    {
                        posxy(f+1,(c+1)*2,pos);
                        printf("%s%s%c\n",pos,"\033[1;41m",m[f][c]);
                        posxy(f+1,((c+1)*2)-1,pos);
                        printf("%s%s%c\n",pos,"\033[1;41m",m[f][c]);
                    }
                    else if(com[sim] == 1)
                    {
                        posxy(f+1,(c+4)*2,pos);
                        printf("%s%s%c\n",pos,"\033[1;42m",m[f][c]);
                        posxy(f+1,((c+4)*2)-1,pos);
                        printf("%s%s%c\n",pos,"\033[1;42m",m[f][c]);
                    }
                    else if(com[sim] == 2)
                    {
                        posxy(f+4,(c+1)*2,pos);
                        printf("%s%s%c\n",pos,"\033[1;44m",m[f][c]);
                        posxy(f+4,((c+1)*2)-1,pos);
                        printf("%s%s%c\n",pos,"\033[1;44m",m[f][c]);
                    }
                    else
                    {
                        posxy(f+4,(c+4)*2,pos);
                        printf("%s%s%c\n",pos,"\033[1;43m",m[f][c]);
                        posxy(f+4,((c+4)*2)-1,pos);
                        printf("%s%s%c\n",pos,"\033[1;43m",m[f][c]);
                    }
                }
            }
            espera(tem);
            for(int f = 0; f<fil; f++)
            {
                for(int c = 0; c<fil; c++)
                {
                    posxy(f+1,(c+1)*2,pos);
                    printf("%s%s%c\n",pos,"\033[0;m",m[f][c]);
                    posxy(f+1,((c+1)*2)-1,pos);
                    printf("%s%s%c\n",pos,"\033[0;m",m[f][c]);
                }
            }
            espera(0.2);
        }
        int numer;
        int *comprueba = malloc(sizeof(int)*50);
        for(int sim = 0; sim<cont; sim++)
        {
            posxy(9,1,pos);
            printf("%s%s",pos,"Ingrese uno por uno los numeros en base a los colores:");
            posxy(10,1,pos);
            printf("%s%s",pos,"(1-ROJO)(2-VERDE)(3-AZUL)(4-AMARILLO): ");
            posxy(10,42,pos);
            scanf("%i",&numer);
            posxy(10,42,pos);
            printf("%s%s",pos," ");
            comprueba[sim]=numer-1;
        }
        for(int sim = 0; sim<cont; sim++)
        {
            if(com[sim]!=comprueba[sim])
            {
                aux=1;
            }
        }
        if(cont==49)
        {
            posxy(11,1,pos);
            printf("%s%s",pos,"Has llegado al final, tu memoria es de otro mundo");
            aux=2;
        }
        cont++;
        if(tem>0.2)
        {
            tem-=0.1;
        }
    }
    if(aux==1)
    {
        posxy(11,1,pos);
        printf("%s%s%i\n",pos,"Oh, has perdido, rondas ganadas: ",cont-2);
    }
}

void espera(double seg)
{
    if(seg<0)return;
    #ifdef _WIN32
        seg *= 1000;
        int goal = (int) seg + clock();
        while(goal>clock());
    #else
        seg *= 1000000;
        int goal = (int) seg;
        usleep(goal);
    #endif
}

void posxy(int x, int y, char*v)
{
    char fi[3]={'\0','\0','\0'}, co[3]={'\0','\0','\0'};
    char cadpos[15] = {'\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0'};
    sprintf(fi,"%d",x);
    sprintf(co,"%d",y);
    strcat(cadpos,"\033[");
    strcat(cadpos,fi);
    strcat(cadpos,";");
    strcat(cadpos,co);
    strcat(cadpos,"H");
    strcpy(v,cadpos);
}

char **crearM(int f)
{
    char **matriz;
    matriz = (char**)malloc(sizeof(char**)*f);
    for(int cont = 0; cont<f; cont++)
    {
        matriz[cont] = (char*)malloc(sizeof(char*)*f);
    }
    return(matriz);
}

void cargaM(char **m, int fil)
{
    for(int f = 0; f<fil; f++)
    {
        for(int c = 0; c<fil; c++)
        {
            m[f][c] = ' ';
        }
    }
}

void cargaMemo(char **m, int fil, char* aux)
{
    int num, cont=0;
    char aux2;
    for(int i = 0; i<8; i++)
    {
        aux[i]=(char)i;
        aux[i+8]=(char)i;
    }
    for(int j = 0; j<3; j++)
    {
        for(int i = 0; i<16; i++)
        {
            num = rand() % 16;
            aux2 = aux[i];
            aux[i] = aux[num];
            aux[num] = aux2;
        }
    }
    for(int f = 0; f<fil; f++)
    {
        for(int c = 0; c<fil; c++)
        {
            m[f][c] = aux[cont];
            cont++;
        }
    }
}

char *crearVec(int f)
{
    char *p;
    p = malloc(sizeof(char)*f);
    return(p);
}