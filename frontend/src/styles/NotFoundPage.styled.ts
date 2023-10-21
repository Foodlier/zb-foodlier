import styled from 'styled-components'
import { typography, weight } from '../constants/Styles'

export const Container = styled.div`
  height: calc(100vh - 10rem);
  display: flex;
  align-items: center;
  justify-content: center;
`

export const Title = styled.div`
  font-size: ${typography.web.mainTitle};
  font-weight: ${weight.mainTitle};
`
