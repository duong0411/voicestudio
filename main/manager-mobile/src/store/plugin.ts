import type { AgentFunction, PluginDefinition } from '@/api/agent/types'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePluginStore = defineStore(
  'plugin',
  () => {

    const allFunctions = ref<PluginDefinition[]>([])


    const currentFunctions = ref<AgentFunction[]>([])


    const currentAgentId = ref('')


    const setAllFunctions = (functions: PluginDefinition[]) => {
      allFunctions.value = functions
    }


    const setCurrentFunctions = (functions: AgentFunction[]) => {
      currentFunctions.value = functions
    }


    const setCurrentAgentId = (agentId: string) => {
      currentAgentId.value = agentId
    }


    const updateFunctions = (functions: AgentFunction[]) => {
      currentFunctions.value = functions
    }


    const clear = () => {
      allFunctions.value = []
      currentFunctions.value = []
      currentAgentId.value = ''
    }

    return {
      allFunctions,
      currentFunctions,
      currentAgentId,
      setAllFunctions,
      setCurrentFunctions,
      setCurrentAgentId,
      updateFunctions,
      clear,
    }
  },
  {
    persist: false,
  },
)
