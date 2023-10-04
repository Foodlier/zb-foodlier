import styled from 'styled-components'
import { palette, breakpoints } from '../../../constants/Styles'

export const ProcedureContainer = styled.section`
  padding-bottom: 11rem;

  ${breakpoints.large} {
    margin-top: 5rem;
  }
`

export const MainTit = styled.h1`
  width: 100%;
  font-size: 2.5rem;
  font-weight: 800;
  padding: 0.5rem 2rem;
  margin: 2rem 0 2rem;
  border-bottom: 0.2rem solid ${palette.divider};

  ${breakpoints.large} {
    margin-top: 0;
  }
`

export const ProcedureWrap = styled.ol`
  padding: 0 2rem;
`

export const ProcedureBox = styled.li`
  display: flex;
  justify-content: space-between;
  padding-bottom: 1rem;
`

export const ImgWrap = styled.div`
  display: flex;
  width: 10rem;
  height: 10rem;
  min-width: 10rem;
  min-height: 10rem;
  justify-content: center;
  align-items: center;
  margin-bottom: 2rem;
  border-radius: 1rem;
  overflow: hidden;
  margin-left: 1rem;

  ${breakpoints.large} {
    min-width: 15rem;
    min-height: 15rem;
  }
`

export const ProcedureImg = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`

export const stepTxt = styled.p``
