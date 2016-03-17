/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Active.Contestant;
import Passive.ContestantsBench;
import Passive.RefereeSite;
import java.util.Set;

/**
 * General Description:
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */

public interface CoachStrategy {
    public Set<Integer> pickTeam(ContestantsBench bench, RefereeSite site);
}
