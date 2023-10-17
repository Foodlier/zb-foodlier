import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const Container = styled.div`
  width: 100%;
  height: 440px;
  background-color: teal;
`

export const Marker = styled.div`
  display: flex;
  width: 30rem;
  flex-direction: column;
  background-color: ${palette.white};
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  padding: 2rem;
`

export const FlexWrap = styled.div`
  display: flex;
  align-items: center;
`

export const MarkerTitle = styled.span`
  color: ${palette.textPrimary};
  font-weight: 800;
  font-size: 1.8rem;
  margin-right: 0.5rem;
`

export const MarkerRate = styled.span`
  font-size: 1.4rem;
  color: ${palette.textSecondary};
  margin-left: 0.5rem;
`

export const MarkerSubTitle = styled.span`
  color: ${palette.textSecondary};
  font-size: 1.4rem;
  margin: 1rem 0;
`

export const MarkerButton = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 1rem;
  padding: 0.5rem 0;
  font-size: 1.4rem;
`
