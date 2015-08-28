import java.util.Random;
import Agent.Agent;
import Interaction.Interaction;
import RandamPackage.*;

class LooselyStabilizing_LE_simulator{
	public static final int Gridsize = 100;
	public static final int Roundnum = 1000000;
	public static final int n = 20;
	
	public static String RandomMethod = "Torus";	//Torus or RWP(Random Way Point)
	
	public static void main(String args[]){
		Random random = new Random();
		Agent agent[] = new Agent[n];
		int CTcounter = 0;
		int CT = 0, HT = 0;
		int SPperRound[] = new int[Roundnum];
		
		for(int i=0; i<n; i++)
		agent[i] = new Agent(random.nextBoolean(), random.nextInt(Gridsize)+random.nextDouble(), random.nextInt(Gridsize)+random.nextDouble());
		
		for(int i=0; i<Roundnum; i++){
			int leadercount=0;
			int leaderid=0;
			for(int j=0; j<n; j++) if(agent[j].IsLeader()){ leaderid = j; leadercount++; }
			if(leadercount==1){ 
					SPperRound[i] = leaderid;
					if(SPperRound[i-1]==leaderid){
						HT++;
					}
					else{
						HT = 0; CT = CTcounter;
						System.out.println(leaderid); 
					}
				}
			System.out.println("the number of leaders = " + leadercount);
			int p, q;
			while(true){					//�𗬂�����̂�I��
				p = random.nextInt(n-1);		//�����_����agent���Ƃ��Ă���
				q = RandomWay.RandamPickNearAgent( p, n, agent);		//p�Ƌ���1�ȓ��ɂ���m�[�h�̒���(���id�̒Ⴂ)�m�[�h��q�ɑ��
				if(q != -1) { 	//q������������interaction�����Ď��̃��E���h��
					Interaction.interaction(agent[p], agent[q]);	//�𗬂�����
					for(int j=0; j<n; j++) agent[j].Countdown();	//timer���J�E���g����
					break;						//���̃��E���h��
				}
				for(int j=0; j<n; j++){					//�e�̈ړ�
					/*変更箇所*/
					if(RandomMethod=="Torus"){
						agent[j].Vchange((random.nextDouble()-0.5)*2 , (random.nextDouble()-0.5)*2 );
					}
					else if(RandomMethod == "RWP") {
						double movex = 0, movey = 0;
						if(i==0 || Math.sqrt( (agent[j].getdestx()-agent[j].getx())*(agent[j].getdestx()-agent[j].getx()) - (agent[j].getdesty()-agent[j].gety())*(agent[j].getdesty()-agent[j].gety())) < 1){
							agent[j].DestinationSet(random.nextInt(Gridsize-1) + random.nextDouble(), random.nextInt(Gridsize-1) + random.nextDouble());
						}
						movex = agent[j].getx()/Math.sqrt( (agent[j].getdestx()-agent[j].getx())*(agent[j].getdestx()-agent[j].getx()) - (agent[j].getdesty()-agent[j].gety())*(agent[j].getdesty()-agent[j].gety()));
						movey = agent[j].gety()/Math.sqrt( (agent[j].getdestx()-agent[j].getx())*(agent[j].getdestx()-agent[j].getx()) - (agent[j].getdesty()-agent[j].gety())*(agent[j].getdesty()-agent[j].gety()));
						agent[j].Vchange( movex, movey);
					}
					/**/
					agent[j].ShiftPointForTorus(Gridsize);
				}
			}
			CTcounter++;
			if(CT!=0 && HT!=0 && HT+CT+1 < CTcounter) break;	//HT���I����Ă瑦�I������������
		}
		System.out.println("CT = " + CT);
		System.out.println("HT = " + HT);
	}
}
